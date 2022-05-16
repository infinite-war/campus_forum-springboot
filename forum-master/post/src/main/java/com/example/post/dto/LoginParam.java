package com.example.post.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 登录参数
 */
@Data
public class LoginParam {

    //用户名
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20)
    private String username;

    //密码
    @NotBlank(message = "密码不能为空")
    @Length(min = 6)
    private String password;

}