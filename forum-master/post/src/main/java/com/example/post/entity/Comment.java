package com.example.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    //评论ID
    @TableId(value = "comment_id", type = IdType.INPUT)
    private Long commentId;

    //所属楼层ID
    private Long belongFloorId;

    //评论编号
    private Integer commentNumber;

    //用户ID
    private Long userId;

    //用户昵称（comment表中不存在）
    @TableField(exist = false)
    private String nickname;

    //点赞数
    private Integer likes;

    //内容
    private String content;

    //发布时间
    private LocalDateTime createTime;

    //当前用户是否点过赞（comment表中不存在）
    @TableField(exist = false)
    private Boolean liked;

}
