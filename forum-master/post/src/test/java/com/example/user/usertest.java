package com.example.user;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.entity.PageResult;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.post.dto.PagingParam;
import com.example.post.entity.User;
import com.example.post.mapper.UserMapper;
import com.example.post.util.WrapperOrderPlugin;
import org.junit.jupiter.api.Test;

import java.util.List;

public class usertest {

    private UserMapper userMapper;

    @Test
    public void getPage(){

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        PagingParam pagingParam=new PagingParam();
//        pagingParam.setPage(1);
//        pagingParam.setSize(3);
//
//        WrapperOrderPlugin.addOrderToUserWrapper(queryWrapper, pagingParam.getOrder());
//        //对用户进行分页处理
//        IPage<User> page = new Page<>(pagingParam.getPage(), pagingParam.getSize());
//        IPage<User> result = userMapper.selectPage(page, queryWrapper);
//        List<User> userList = result.getRecords();
//        System.out.println(userList);

// //       填充PageResult
//        PageResult<User> userResult = new PageResult<>();
//        userResult.setRecords(userList);
//        userResult.setTotal(userList.size());

//        Page<User> page = new Page<>(1, 2);
//        Page<User> pageResult = userMapper.testPage(page);
//        System.out.println(JSON.toJSONString(pageResult));
//        // 分页查第二页
//        page = new Page<>(2, 2);
//        pageResult = userMapper.testPage(page);
//        System.out.println(JSON.toJSONString(pageResult));


    }
}
