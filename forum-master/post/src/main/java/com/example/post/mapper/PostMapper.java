package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.post.entity.Post;
import org.apache.ibatis.annotations.Update;

/**
 * 帖子Mapper
 */
public interface PostMapper extends BaseMapper<Post> {

    //删除当前帖子中的一个楼层
    @Update({"update `post` set `floors`=`floors`-1 where post_id = #{postId}"})
    int removeFloorFromPost(Long postId);

    //给当前的帖子添加楼层
    @Update({"update `post` set `floors`=`floors`+1, `total_floors`=`total_floors`+1, `update_time`=now() where post_id = #{postId}"})
    int addFloorToPost(Long postId);

    //帖子浏览数+1
    @Update({"update `post` set `views`=`views`+#{delta} where post_id = #{postId}"})
    int increaseViewsBySpecifiedAmount(Long postId, Long delta);

    //点赞帖子
    @Update({"update `post` set `likes`=`likes`+1 where post_id = #{postId}"})
    int increasePostLikes(Long postId);

    //取消赞
    @Update({"update `post` set `likes`=`likes`-1 where post_id = #{postId}"})
    int decreasePostLikes(Long postId);

}
