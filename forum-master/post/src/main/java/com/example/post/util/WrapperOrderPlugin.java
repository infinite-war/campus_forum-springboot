package com.example.post.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.post.entity.Floor;
import com.example.post.entity.Post;
import com.example.post.entity.User;

/**
 * 给Wrapper增加排序规则
 */
public class WrapperOrderPlugin {

    public static void addOrderToPostWrapper(QueryWrapper<Post> queryWrapper, Integer order) {
        queryWrapper.orderByDesc("update_time");
        //依据浏览数、获赞数、楼层数排序
        queryWrapper.orderByDesc("views");
        queryWrapper.orderByDesc("likes");
        queryWrapper.orderByDesc("floors");
    }

    public static void addOrderToFloorWrapper(QueryWrapper<Floor> queryWrapper, Integer order) {
        //按照floor_number升序。
        queryWrapper.orderByAsc("floor_number");
    }

    public static void addOrderToUserWrapper(QueryWrapper<User> queryWrapper, Integer order) {
        //按照user_id升序。
        queryWrapper.orderByDesc("user_id");
    }

}


