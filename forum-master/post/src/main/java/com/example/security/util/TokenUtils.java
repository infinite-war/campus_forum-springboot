package com.example.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Token工具（对JWT基本操作封装）
 * 和cookie一样，token用于确保有状态访问（原始的http是无状态访问）
 */
@Component
public class TokenUtils {

    private final JwtUtils jwtUtils;

    @Autowired
    public TokenUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String createToken(Map<String, String> claims) {
        return jwtUtils.getToken(claims);
    }

    public Map<String, Object> getMapFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            jwtUtils.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jwtUtils.getTokenInfo(token);
    }

    public Long getUserIdFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            jwtUtils.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //将token转换成map对象
        Map<String, Object> tokenInfo = jwtUtils.getTokenInfo(token);
        return Long.parseLong((String) tokenInfo.get("id"));
    }
}
