package com.example.security.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5加密工具类
 * 相关加密算法有现成的库，这里只做操作封装以供调用
 */
public class Md5Utils {

    //加密
    public static String encode(String password) {
        for (int i = 0; i < 5; i++) {
            password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        }
        return password;
    }

    //验证
    public static Boolean verify(String password, String md5) {
        for (int i = 0; i < 5; i++) {
            password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        }
        return password.equals(md5);
    }
}
