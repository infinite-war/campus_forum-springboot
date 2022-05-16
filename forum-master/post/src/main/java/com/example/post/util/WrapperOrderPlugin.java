package com.example.post.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.post.entity.Floor;
import com.example.post.entity.Post;

/**
 * 给Wrapper增加排序规则
 */
public class WrapperOrderPlugin {

    public static void addOrderToPostWrapper(QueryWrapper<Post> queryWrapper, Integer order) {
        //暂无明确排序方式，所以都按照update_time降序。
        queryWrapper.orderByDesc("update_time");
    }

    public static void addOrderToFloorWrapper(QueryWrapper<Floor> queryWrapper, Integer order) {
        //暂无明确排序方式，所以都按照floor_number升序。
        queryWrapper.orderByAsc("floor_number");
    }

}


