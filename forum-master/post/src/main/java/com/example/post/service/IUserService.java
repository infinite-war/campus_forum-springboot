package com.example.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.Result;
import com.example.post.dto.LoginParam;
import com.example.post.dto.PasswordModification;
import com.example.post.dto.UserModification;
import com.example.post.entity.User;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {

    //用户注册
    Result register(LoginParam loginParam);

    //用户登录
    Result login(LoginParam loginParam);

    //获取自身信息
    Result getOwnInfo(String token);

    //获取完整用户信息
    Result getUserInfo(Long userId);

    //修改用户信息
    Result modifyOwnInfo(String token, UserModification userModification);

    //修改用户密码
    Result modifyOwnPassword(String token, PasswordModification passwordModification);

}
