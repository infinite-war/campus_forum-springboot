package com.example.post.util;

import com.example.post.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Redis定时任务类
 */
@Slf4j  //日志插件
@Component
public class ScheduledService {

    private RedisUtils redisUtils;

    private PostMapper postMapper;

    @Autowired
    public ScheduledService(RedisUtils redisUtils, PostMapper postMapper) {
        this.redisUtils = redisUtils;
        this.postMapper = postMapper;
    }

    //每过5秒就将缓存中的浏览量写入数据库
    @Scheduled(fixedRate = 5 * 1000)
    public void storeViewsInTheDatabase() {
        Map<String, Integer> map = redisUtils.getPostViewsEntry();
        if (!map.isEmpty()) {
            //日志中记录写入事件
            log.info("即将把Redis中的帖子访问量写入MySQL，其内容为" + map);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Long postId = Long.parseLong(entry.getKey());
                try {
                    postMapper.increaseViewsBySpecifiedAmount(postId, Long.valueOf(entry.getValue()));
                } catch (Exception e) {
                    log.info("浏览量写入MySQL时出现异常，异常为" + e.getMessage() + "，帖子ID为" + postId);
                }
                redisUtils.deletePostViewsInCache(postId);
            }
        }
    }


}