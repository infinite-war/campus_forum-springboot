package com.example.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    //帖子ID
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;

    //分区
    private Integer category;

    //标题
    private String title;

    //内容
    private String content;

    //用户ID
    private Long userId;

    //用户昵称（post表中不存在）
    @TableField(exist = false)
    private String nickname;

    //浏览量
    private Long views;

    //点赞数
    private Integer likes;

    //发布时间
    private LocalDateTime createTime;

    //最后回复时间
    private LocalDateTime updateTime;

    //当前楼层数
    private Integer floors;

    //历史楼层数
    private Integer totalFloors;

    //楼层列表（post表中不存在）
    @TableField(exist = false)
    private List<Floor> floorList;

    //当前用户是否点过赞（post表中不存在）
    @TableField(exist = false)
    private Boolean liked;

}
