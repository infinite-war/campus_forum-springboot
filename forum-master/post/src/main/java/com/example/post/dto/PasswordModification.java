package com.example.post.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 修改用户密码参数
 */
@Data
public class PasswordModification {

    //用户ID
    @Min(0)
    private Long userId;

    //旧密码
    @NotBlank
    @Length(min = 6)
    private String oldPassword;

    //新密码
    @NotBlank
    @Length(min = 6)
    private String newPassword;
}
