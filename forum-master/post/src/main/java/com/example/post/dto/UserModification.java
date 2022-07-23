package com.example.post.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 修改用户信息参数
 */
@Data
public class UserModification {

    //用户ID
    @Min(0)
    private Long userId;

    //昵称
    @NotBlank
    private String nickname;

    //性别
    @Min(0)
    @Max(2)
    private Integer gender;

    //学校
    @NotBlank
    @Length(max = 10)
    private String college;

    //生日
    private LocalDate birthday;

    //手机号
    @NotBlank
    @Length(max = 20)
    private String phone;

    //邮箱
    @NotBlank
    @Length(max = 30)
    private String email;

    //简介
    @Length(max = 200)
    private String introduction;

}
