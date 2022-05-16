package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.PageResult;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.common.util.IdWorker;
import com.example.post.dto.NewPost;
import com.example.post.dto.PagingParam;
import com.example.post.dto.SearchParam;
import com.example.post.entity.Comment;
import com.example.post.entity.Floor;
import com.example.post.entity.Post;
import com.example.post.mapper.CommentMapper;
import com.example.post.mapper.FloorMapper;
import com.example.post.mapper.PostMapper;
import com.example.post.mapper.UserMapper;
import com.example.post.service.IPostService;
import com.example.post.util.RedisUtils;
import com.example.post.util.WrapperOrderPlugin;
import com.example.post.views.PublishPost;
import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子服务实现类
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    private UserMapper userMapper;

    private PostMapper postMapper;

    private FloorMapper floorMapper;

    private CommentMapper commentMapper;

    private RedisUtils redisUtils;

    private IdWorker idWorker;

    private TokenUtils tokenUtils;

    @Autowired
    public PostServiceImpl(UserMapper userMapper, PostMapper postMapper, FloorMapper floorMapper, CommentMapper commentMapper, RedisUtils redisUtils, IdWorker idWorker, TokenUtils tokenUtils) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.floorMapper = floorMapper;
        this.commentMapper = commentMapper;
        this.redisUtils = redisUtils;
        this.idWorker = idWorker;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public Result publishPost(String token, NewPost newPost) {
        Post post = new Post();
        Long postId = idWorker.nextId();
        Long userId = tokenUtils.getUserIdFromToken(token);
        post.setPostId(postId);
        post.setCategory(newPost.getCategory());
        post.setTitle(newPost.getTitle());
        post.setContent(newPost.getContent());
        post.setUserId(userId);
        post.setViews(0L);
        post.setLikes(0);
        LocalDateTime now = LocalDateTime.now();
        post.setCreateTime(now);
        post.setUpdateTime(now);
        post.setFloors(0);
        post.setTotalFloors(0);
        postMapper.insert(post);
        return new Result(true, StatusCode.OK, "帖子发布成功", new PublishPost(postId));
    }

    @Override
    @Transactional
    public Result deletePost(String token, Long postId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return new Result(false, StatusCode.PARAM_ERROR, "删除失败，指定的帖子不存在");
        }
        if (!userId.equals(post.getUserId())) {
            return new Result(false, StatusCode.ACCESS_ERROR, "删除失败，无权操作");
        }
        //先清空这个帖子下的所有楼层
        List<Floor> floorList = floorMapper.selectList(new QueryWrapper<Floor>().eq("belong_post_id", postId));
        for (Floor floor : floorList) {
            commentMapper.delete(new QueryWrapper<Comment>().eq("belong_floor_id", floor.getFloorId()));
            floorMapper.deleteById(floor);
        }
        postMapper.deleteById(postId);

        return new Result(true, StatusCode.OK, "删除成功");
    }

    @Override
    public Result getPostDetail(String token, Long postId, PagingParam pagingParam) {
        //判断用户是否处于属于登录状态
        boolean loginStatus = false;
        Long userId = null;
        if (token != null) {
            loginStatus = true;
            userId = tokenUtils.getUserIdFromToken(token);
        }
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return new Result(false, StatusCode.PARAM_ERROR, "获取失败，指定的帖子不存在");
        }
        //每次请求帖子详情页的第一页时，访问量便加一
        if (pagingParam.getPage() == 1) {
            redisUtils.increasePostViews(postId);
        }
        post.setNickname(userMapper.getNicknameById(post.getUserId()));
        if (loginStatus) {
            post.setLiked(redisUtils.queryUserIsLike(userId, postId));
        }
        //加载这个帖子下的楼层
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong_post_id", postId);
        WrapperOrderPlugin.addOrderToFloorWrapper(queryWrapper, pagingParam.getOrder());
        //对楼层进行分页处理
        IPage<Floor> page = new Page<>(pagingParam.getPage(), pagingParam.getSize());
        IPage<Floor> result = floorMapper.selectPage(page, queryWrapper);
        List<Floor> floorList = result.getRecords();
        post.setFloorList(floorList);
        for (Floor floor : floorList) {
            floor.setNickname(userMapper.getNicknameById(floor.getUserId()));
            if (loginStatus) {
                floor.setLiked(redisUtils.queryUserIsLike(userId, floor.getFloorId()));
            }
            //没有出评论分页功能，所以这里展示所有的评论，有多少评论就展示多少
            List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>()
                    .eq("belong_floor_id", floor.getFloorId()).orderByAsc("comment_number"));
            floor.setCommentList(commentList);
            for (Comment comment : commentList) {
                comment.setNickname(userMapper.getNicknameById(comment.getUserId()));
                if (loginStatus) {
                    comment.setLiked(redisUtils.queryUserIsLike(userId, comment.getCommentId()));
                }
            }
        }
        return new Result(true, StatusCode.OK, "获取成功", post);
    }

    @Override
    public Result getPostList(String token, SearchParam searchParam, PagingParam pagingParam) {
        //判断用户是否处于属于登录状态
        boolean loginStatus = false;
        Long userId = null;
        if (token != null) {
            loginStatus = true;
            userId = tokenUtils.getUserIdFromToken(token);
        }
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        //如果SearchParam存在参数，则加入QueryWrapper中作为查询条件
        if ((searchParam.getKeyword() != null) && (!searchParam.getKeyword().isBlank())) {
            queryWrapper.like("title", searchParam.getKeyword());
        }
        if (searchParam.getUserId() != null) {
            queryWrapper.eq("user_id", searchParam.getUserId());
        }
        if (searchParam.getCategory() != null) {
            queryWrapper.eq("category", searchParam.getCategory());
        }
        WrapperOrderPlugin.addOrderToPostWrapper(queryWrapper, pagingParam.getOrder());
        //对帖子进行分页处理
        IPage<Post> page = new Page<>(pagingParam.getPage(), pagingParam.getSize());
        IPage<Post> result = postMapper.selectPage(page, queryWrapper);
        List<Post> postList = result.getRecords();
        for (Post post : postList) {
            post.setNickname(userMapper.getNicknameById(post.getUserId()));
            if (loginStatus) {
                post.setLiked(redisUtils.queryUserIsLike(userId, post.getPostId()));
            }
        }
        //填充PageResult
        PageResult<Post> pageResult = new PageResult<>();
        pageResult.setRecords(postList);
        pageResult.setTotal(postList.size());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @Override
    public Result likeThePost(String token, Long postId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        boolean liked = redisUtils.queryUserIsLike(userId, postId);
        if (!liked) {
            redisUtils.addUserLike(userId, postId);
            postMapper.increasePostLikes(postId);
            return new Result(true, StatusCode.OK, "点赞成功");
        }
        return new Result(true, StatusCode.REP_ERROR, "已经给帖子点赞，无法再次点赞");
    }

    @Override
    public Result dislikeThePost(String token, Long postId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        boolean liked = redisUtils.queryUserIsLike(userId, postId);
        if (liked) {
            redisUtils.removeUserLike(userId, postId);
            postMapper.decreasePostLikes(postId);
            return new Result(true, StatusCode.OK, "取消点赞成功");
        }
        return new Result(true, StatusCode.REP_ERROR, "尚未给帖子点赞，无法取消点赞");
    }

}