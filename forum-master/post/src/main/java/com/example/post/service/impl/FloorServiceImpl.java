package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.common.util.IdWorker;
import com.example.post.dto.NewFloor;
import com.example.post.entity.Comment;
import com.example.post.entity.Floor;
import com.example.post.entity.Post;
import com.example.post.mapper.CommentMapper;
import com.example.post.mapper.FloorMapper;
import com.example.post.mapper.PostMapper;
import com.example.post.mapper.UserMapper;
import com.example.post.service.IFloorService;
import com.example.post.util.RedisUtils;
import com.example.post.views.PublishFloor;
import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 楼层服务实现类
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService {

    private UserMapper userMapper;

    private PostMapper postMapper;

    private FloorMapper floorMapper;

    private CommentMapper commentMapper;

    private RedisUtils redisUtils;

    private IdWorker idWorker;

    private TokenUtils tokenUtils;

    @Autowired
    public FloorServiceImpl(UserMapper userMapper, PostMapper postMapper, FloorMapper floorMapper, CommentMapper commentMapper, RedisUtils redisUtils, IdWorker idWorker, TokenUtils tokenUtils) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.floorMapper = floorMapper;
        this.commentMapper = commentMapper;
        this.redisUtils = redisUtils;
        this.idWorker = idWorker;
        this.tokenUtils = tokenUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result publishFloor(String token, NewFloor newFloor) {
        Long belongPostId = newFloor.getPostId();
        Post belongPost = postMapper.selectById(belongPostId);
        if (belongPost == null) {
            return new Result(true, StatusCode.OK, "发布失败，指定的帖子不存在");
        }
        Floor floor = new Floor();
        Long floorId = idWorker.nextId();
        floor.setFloorId(floorId);
        floor.setBelongPostId(belongPostId);
        floor.setFloorNumber(belongPost.getTotalFloors() + 1);
        floor.setUserId(tokenUtils.getUserIdFromToken(token));
        floor.setLikes(0);
        floor.setContent(newFloor.getContent());
        LocalDateTime now = LocalDateTime.now();
        floor.setCreateTime(now);
        floor.setComments(0);
        floor.setTotalComments(0);
        floorMapper.insert(floor);
        postMapper.addFloorToPost(belongPostId);
        return new Result(true, StatusCode.OK, "发布楼层成功", new PublishFloor(floorId, floor.getFloorNumber()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteFloor(String token, Long floorId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        Floor floor = floorMapper.selectById(floorId);
        if (floor == null) {
            return new Result(false, StatusCode.PARAM_ERROR, "删除失败，指定的楼层不存在");
        }
        if (!userId.equals(floor.getUserId())) {
            return new Result(false, StatusCode.ACCESS_ERROR, "删除失败，无权操作");
        }
        commentMapper.delete(new QueryWrapper<Comment>().eq("belong_floor_id", floorId));
        floorMapper.deleteById(floorId);
        postMapper.removeFloorFromPost(floor.getBelongPostId());
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @Override
    public Result likeTheFloor(String token, Long floorId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        boolean liked = redisUtils.queryUserIsLike(userId, floorId);
        if (!liked) {
            redisUtils.addUserLike(userId, floorId);
            floorMapper.increaseFloorLikes(floorId);
            return new Result(true, StatusCode.OK, "点赞成功");
        }
        return new Result(true, StatusCode.REP_ERROR, "已经给楼层点赞，无法再次点赞");
    }

    @Override
    public Result dislikeTheFloor(String token, Long floorId) {
        Long userId = tokenUtils.getUserIdFromToken(token);
        boolean liked = redisUtils.queryUserIsLike(userId, floorId);
        if (liked) {
            redisUtils.removeUserLike(userId, floorId);
            floorMapper.decreaseFloorLikes(floorId);
            return new Result(true, StatusCode.OK, "取消点赞成功");
        }
        return new Result(true, StatusCode.REP_ERROR, "尚未给楼层点赞，无法取消点赞");
    }

}
