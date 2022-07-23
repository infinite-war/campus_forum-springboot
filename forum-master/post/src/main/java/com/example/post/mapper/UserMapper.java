package com.example.post.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.post.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    //获取用户的昵称
    @Select({"select `nickname` from user where user_id = #{userId}"})
    String getNicknameById(Long userId);

    //帖子发布数+1
    @Update({"update `user` set `published`=`published`+ 1 where user_id = #{userId}"})
    int increasePublishedNums(Long userId);

    //帖子发布数-1
    @Update({"update `user` set `published`=`published`- 1 where user_id = #{userId}"})
    int decreasePublishedNums(Long userId);

    //获赞数+1
    @Update({"update `user` set `likes`=`likes`+ 1 where user_id = #{userId}"})
    int increaseLikesNums(Long userId);

    //获赞数+1
    @Update({"update `user` set `likes`=`likes`- 1 where user_id = #{userId}"})
    int decreaseLikesNums(Long userId);
}
