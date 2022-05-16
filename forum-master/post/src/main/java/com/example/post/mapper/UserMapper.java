package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.post.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    //获取用户的昵称
    @Select({"select `nickname` from user where user_id = #{userId}"})
    String getNicknameById(Long userId);
}
