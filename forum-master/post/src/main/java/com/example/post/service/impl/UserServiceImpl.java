package com.example.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.post.dto.LoginParam;
import com.example.post.dto.PasswordModification;
import com.example.post.dto.UserModification;
import com.example.post.entity.User;
import com.example.post.mapper.UserMapper;
import com.example.post.service.IUserService;
import com.example.post.views.UserOutline;
import com.example.security.util.Md5Utils;
import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private UserMapper userMapper;

    private TokenUtils tokenUtils;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, TokenUtils tokenUtils) {
        this.userMapper = userMapper;
        this.tokenUtils = tokenUtils;
    }


    @Override
    public Result getOwnInfo(String token) {
        Long id = tokenUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(id);
        return new Result(true, StatusCode.OK, "获取成功", new UserOutline(user.getUserId(), user.getNickname()));
    }

    @Override
    public Result modifyOwnInfo(String token, UserModification userModification) {
        Long id = tokenUtils.getUserIdFromToken(token);
        User expectedUser = new User();
        expectedUser.setUserId(id);
        expectedUser.setNickname(userModification.getNickname());
        expectedUser.setGender(userModification.getGender());
        expectedUser.setCollege(userModification.getCollege());
        expectedUser.setBirthday(userModification.getBirthday());
        expectedUser.setPhone(userModification.getPhone());
        expectedUser.setIntroduction(userModification.getIntroduction());
        userMapper.updateById(expectedUser);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @Override
    public Result getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return new Result(false, StatusCode.ERROR, "指定的用户不存在");
        }
        // 这三个选项设为空值，前端收到的返回值中不会显示
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
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
        // 把明文密码加密后进行存储
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
        userMapper.insert(user);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    @Override
    public Result login(LoginParam loginParam) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", loginParam.getUsername()));
        if (user == null) {
            return new Result(false, StatusCode.LOGIN_ERROR, "用户不存在");
        }
        Boolean verify = Md5Utils.verify(loginParam.getPassword(), user.getPassword());
        if (!verify) {
            return new Result(false, StatusCode.LOGIN_ERROR, "密码错误");
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id", user.getUserId().toString());
        stringStringHashMap.put("username", user.getUsername());
        stringStringHashMap.put("role", user.getRole().toString());
        String token = tokenUtils.createToken(stringStringHashMap);
        return new Result(true, StatusCode.OK, "登录成功", new UserOutline(user.getUserId(), user.getNickname(), token));
    }

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


}
