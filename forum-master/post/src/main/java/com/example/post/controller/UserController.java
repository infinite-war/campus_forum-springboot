package com.example.post.controller;


import com.example.common.entity.Result;
import com.example.post.dto.LoginParam;
import com.example.post.dto.PasswordModification;
import com.example.post.dto.UserModification;
import com.example.post.service.IUserService;
import com.example.security.aspect.UserRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }


    //注册请求
    @PostMapping("/register")
    public Result register(@Valid @RequestBody LoginParam loginParam) {
        return userService.register(loginParam);
    }

    //登录请求
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginParam loginParam) {
        return userService.login(loginParam);
    }

    //获取id，昵称两个基本用户信息请求
    @UserRequired
    @GetMapping
    public Result getOwnInfo(@RequestHeader String token) {
        return userService.getOwnInfo(token);
    }

    //修改用户信息请求
    @UserRequired
    @PutMapping
    public Result modifyOwnInfo(@RequestHeader String token, @Valid @RequestBody UserModification userModification) {
        return userService.modifyOwnInfo(token, userModification);
    }

    //获取具体用户信息请求
    @GetMapping("/{userId}")
    public Result getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    //修改密码请求
    @UserRequired
    @PutMapping("/password")
    public Result modifyOwnPassword(@RequestHeader String token, @Valid @RequestBody PasswordModification passwordModification) {
        return userService.modifyOwnPassword(token, passwordModification);
    }


}

