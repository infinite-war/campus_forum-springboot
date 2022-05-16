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
 * 楼层实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Floor implements Serializable {

    //楼层ID
    @TableId(value = "floor_id", type = IdType.INPUT)
    private Long floorId;

    //所属帖子ID
    private Long belongPostId;

    //楼层编号
    private Integer floorNumber;

    //盖楼的用户ID
    private Long userId;

    //用户昵称（floor表中不存在）
    @TableField(exist = false)
    private String nickname;

    //点赞数
    private Integer likes;

    //具体内容
    private String content;

    //发布时间
    private LocalDateTime createTime;

    //当前评论数
    private Integer comments;

    //历史评论数
    private Integer totalComments;

    //评论列表（floor表中不存在）
    @TableField(exist = false)
    private List<Comment> commentList;

    //当前用户是否点过赞（floor表中不存在）
    @TableField(exist = false)
    private Boolean liked;


}
