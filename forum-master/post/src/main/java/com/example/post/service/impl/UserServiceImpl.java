package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.PageResult;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.post.dto.*;
import com.example.post.entity.Comment;
import com.example.post.entity.Floor;
import com.example.post.entity.Post;
import com.example.post.entity.User;
import com.example.post.mapper.CommentMapper;
import com.example.post.mapper.FloorMapper;
import com.example.post.mapper.PostMapper;
import com.example.post.mapper.UserMapper;
import com.example.post.service.IUserService;
import com.example.post.util.WrapperOrderPlugin;
import com.example.post.views.UserOutline;
import com.example.security.util.Md5Utils;
import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final FloorMapper floorMapper;
    private final CommentMapper commentMapper;

    private final TokenUtils tokenUtils;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PostMapper postMapper ,
                           FloorMapper floorMapper,CommentMapper commentMapper,TokenUtils tokenUtils) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.floorMapper=floorMapper;
        this.commentMapper=commentMapper;
        this.tokenUtils = tokenUtils;
    }


    @Override
    public Result getOwnInfo(String token) {
        Long id = tokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(id);
        // Result类会将传入参数包装成json字符串传输到前端
        return new Result(true, StatusCode.OK, "获取成功", new UserOutline(user.getUserId(), user.getNickname(),user.getRole().toString()));
    }

    @Override
    public Result modifyOwnInfo(String token, UserModification userModification) {
        Long id = tokenUtils.getUserIdFromToken(token);
        //新建对象封装修改信息
        User expectedUser = new User();
        expectedUser.setUserId(id);
        expectedUser.setNickname(userModification.getNickname());
        expectedUser.setGender(userModification.getGender());
        expectedUser.setCollege(userModification.getCollege());
        expectedUser.setBirthday(userModification.getBirthday());
        expectedUser.setPhone(userModification.getPhone());
        expectedUser.setEmail(userModification.getEmail());
        expectedUser.setIntroduction(userModification.getIntroduction());
        //将修改信息传输到数据库执行修改操作
        userMapper.updateById(expectedUser);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @Override
    public Result getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return new Result(false, StatusCode.ERROR, "指定的用户不存在");
        }
        // 这三个选项修改为空值，前端收到的json串中不会显示
        user.setUsername(null);
        user.setPassword(null);
        //user.setRole(null);
        return new Result(true, StatusCode.OK, "查询成功", user);
    }

    @Override
    public Result register(LoginParam loginParam) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", loginParam.getUsername()));
        if (user != null) {
            return new Result(false, StatusCode.REP_ERROR, "该用户名已被使用");
        }
        user = new User();
        user.setUsername(loginParam.getUsername());
        // 把明文密码通过Md5加密后进行存储
        user.setPassword(Md5Utils.encode(loginParam.getPassword()));
        // 初始昵称为username，可以后面再改
        user.setNickname(loginParam.getUsername());
        // 0代表性别未知
        user.setGender(0);
        user.setCollege("未设置");
        // 初始生日为注册日期，可以后面再改
        user.setBirthday(LocalDate.now());
        user.setPhone("未设置");
        user.setIntroduction("未设置");
        // 新人的论坛等级都是一级
        user.setLevel(1);
        user.setPoints(0);
        user.setPublished(0);
        user.setVisits(0L);
        user.setLikes(0);
        // 默认注册的均为普通用户
        user.setRole(0);

        //将创建好的新用户添加到数据库中
        userMapper.insert(user);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    @Override
    public Result login(LoginParam loginParam) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", loginParam.getUsername()));
        if (user == null) {
            return new Result(false, StatusCode.LOGIN_ERROR, "用户不存在");
        }
        //密码验证
        Boolean verify = Md5Utils.verify(loginParam.getPassword(), user.getPassword());
        if (!verify) {
            return new Result(false, StatusCode.LOGIN_ERROR, "密码错误");
        }
        //将查出来的用户基本信息(包括id，username，role)装入map对象
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", user.getUserId().toString());
        stringStringHashMap.put("username", user.getUsername());
        stringStringHashMap.put("role", user.getRole().toString());
        //结合上面三个基本信息给登录用户发放token，后续与后端的交互都要经过token验证
        String token = tokenUtils.createToken(stringStringHashMap);
        return new Result(true, StatusCode.OK, "登录成功", new UserOutline(user.getUserId(), user.getNickname(), user.getRole().toString(), token));
    }

//    @Override
//    public Result logout(String token){
//        return new Result(true, StatusCode.OK, "注销成功");
//    }

    @Override
    public Result modifyOwnPassword(String token, PasswordModification passwordModification) {
        Long id = tokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(id);
        Boolean verify = Md5Utils.verify(passwordModification.getOldPassword(), user.getPassword());
        if (!verify) {
            return new Result(false, StatusCode.LOGIN_ERROR, "原始密码错误");
        }
        user.setPassword(Md5Utils.encode(passwordModification.getNewPassword()));
        userMapper.updateById(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    @Override
    public Result getUserList(String token, SearchParam searchParam, PagingParam pagingParam) {
        //判断用户是否处于属于登录状态
        boolean loginStatus = false;
        Long userId = null;
        if (token != null) {
            loginStatus = true;
            userId = tokenUtils.getUserIdFromToken(token);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //如果SearchParam存在参数，则加入QueryWrapper中作为查询条件
        if (searchParam.getUserId() != null) {
            queryWrapper.eq("user_id", searchParam.getUserId());
        }
        if (searchParam.getKeyword() != null) {
            queryWrapper.like("username", searchParam.getKeyword());
        }

        WrapperOrderPlugin.addOrderToUserWrapper(queryWrapper, pagingParam.getOrder());
        //对用户进行分页处理
        IPage<User> page = new Page<>(pagingParam.getPage(), pagingParam.getSize());
        IPage<User> result = userMapper.selectPage(page, queryWrapper);
        List<User> userList = result.getRecords();

        //填充PageResult
        PageResult<User> userResult = new PageResult<>();
        userResult.setRecords(userList);
        userResult.setTotal(userList.size());

        return new Result(true, StatusCode.OK, "查询成功", userResult);
    }

    @Override
    @Transactional
    public Result deleteUser(String token,Long delUserId) {
        //获取发出删除请求的用户id
        //只有当前id是这个帖子的发布者，才能执行删除操作
        Long userId = tokenUtils.getUserIdFromToken(token);
        User admin=userMapper.selectById(userId);
        if(admin==null || admin.getRole()!=1){
            return new Result(false, StatusCode.ACCESS_ERROR, "删除失败，没有该权限");
        }

        User user = userMapper.selectById(delUserId);
        if (user == null) {
            return new Result(false, StatusCode.PARAM_ERROR, "删除失败，指定的用户不存在");
        }
        delUserId=user.getUserId();

        //清空这个用户的所有已发表内容

        //1.删除评论
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("user_id", delUserId));
        for (Comment comment : commentList) {
            commentMapper.deleteById(comment.getCommentId());
            floorMapper.removeCommentFromFloor(comment.getBelongFloorId());
        }


        //2.删除盖楼
        List<Floor> floorList = floorMapper.selectList(new QueryWrapper<Floor>().eq("user_id", userId));
        for (Floor floor : floorList) {
            commentMapper.delete(new QueryWrapper<Comment>().eq("belong_floor_id", floor.getFloorId()));
            floorMapper.deleteById(floor.getFloorId());
            postMapper.removeFloorFromPost(floor.getBelongPostId());
        }


        //3.删除帖子
        List<Post> postList = postMapper.selectList(new QueryWrapper<Post>().eq("user_id", userId));
        for (Post post : postList) {
            List<Floor> pfloorList = floorMapper.selectList(new QueryWrapper<Floor>().eq("belong_post_id", post.getPostId()));
            for (Floor floor : pfloorList) {
                commentMapper.delete(new QueryWrapper<Comment>().eq("belong_floor_id", floor.getFloorId()));
                floorMapper.deleteById(floor);
            }
            postMapper.deleteById(post);
            userMapper.decreasePublishedNums(userId);
        }

        userMapper.deleteById(delUserId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
