package com.example.post.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息概要视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutline {

    //用户ID
    private Long userId;
    //昵称
    private String nickname;
    //生成的token
    private String token;

    public UserOutline(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
