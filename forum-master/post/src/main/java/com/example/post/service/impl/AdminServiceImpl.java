package com.example.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.Result;
import com.example.post.entity.Comment;
import com.example.post.entity.User;
import com.example.post.mapper.CommentMapper;
import com.example.post.mapper.PostMapper;
import com.example.post.mapper.UserMapper;
import com.example.post.service.IAdminService;

public class AdminServiceImpl extends ServiceImpl<UserMapper, User>  implements IAdminService {

    private UserMapper userMapper;
    private PostMapper postMapper;

    @Override
    public Result getAllUsers() {
        return null;
    }

    @Override
    public Result getAllPosts() {
        return null;
    }
}
