package com.example.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * JWT基本操作类
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtUtils {

    private String sign = "!oas+56*2-1#@qw!@#?2'as";

    private Integer expireDay = 7;

    public String getToken(Map<String, String> claims) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, expireDay);
        return JWT.create().withClaim("user", claims)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(sign));
    }

    public void verify(String token) {
        // 如果验证失败会报错
        JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
    }

    public Map<String, Object> getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
        return verify.getClaim("user").asMap();
    }

}
