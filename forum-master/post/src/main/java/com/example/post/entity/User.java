package com.example.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //用户ID
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    //用户名
    private String username;

    //密码
    private String password;

    //昵称
    private String nickname;

    //性别
    private Integer gender;

    //学校
    private String college;

    //生日
    private LocalDate birthday;

    //手机号
    private String phone;

    //简介
    private String introduction;

    //论坛等级
    private Integer level;

    //论坛积分
    private Integer points;

    //累计发帖
    private Integer published;

    //被访问次数
    private Long visits;

    //被点赞数
    private Integer likes;

    //身份
    private Integer role;

}
