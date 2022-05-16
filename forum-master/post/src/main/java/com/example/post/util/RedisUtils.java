package com.example.post.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Redis工具类
 * （对RedisTemplate操作封装，用到Redis时直接使用这个类即可）
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    //检查指定的用户是否赞过目标id代表的帖子/楼层/评论
    public Boolean queryUserIsLike(Long userId, Long targetId) {
        return redisTemplate.opsForSet().isMember(userId + "Like", targetId);
    }

    //点赞
    public Long addUserLike(Long userId, Long targetId) {
        return redisTemplate.opsForSet().add(userId + "Like", targetId);
    }

    //取消赞
    public Long removeUserLike(Long userId, Long targetId) {
        return redisTemplate.opsForSet().remove(userId + "Like", targetId);
    }

    //浏览数+1
    public Long increasePostViews(Long postId) {
        return redisTemplate.opsForHash().increment("postViews", String.valueOf(postId), 1);
    }

    //
    public Map<String, Integer> getPostViewsEntry() {
        return redisTemplate.opsForHash().entries("postViews");
    }

    public void deletePostViewsInCache(Long postId) {
        redisTemplate.opsForHash().delete("postViews", String.valueOf(postId));
    }

    //发表数+1
    public void increasePublished(Integer published){
        redisTemplate.opsForHash().increment("published",String.valueOf(published),1);
    }

    //发表数-1
    public void decreasePublished(Integer published){
        redisTemplate.opsForHash().increment("published",String.valueOf(published),-1);
    }
}
