package com.example.security.aspect;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.common.entity.Result;
import com.example.common.entity.StatusCode;
import com.example.security.util.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 通过AOP实现鉴权
 *
 * @author 廖菁璞、马婉荣
 */
@Component
@Aspect
public class MemberRequiredAspect {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @Autowired
    JwtUtils jwtUtils;

    @Around("@annotation(com.example.security.aspect.MemberRequired)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        String token = request.getHeader("token");

        if (StringUtils.isEmpty(token)) {
            return new Result(false, StatusCode.ACCESS_ERROR, "token为空", "权限不足");
        }
        Result response = new Result();
        response.setCode(StatusCode.ACCESS_ERROR);
        response.setMessage("权限不足");
        try {
            jwtUtils.verify(token);
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            response.setData("无效签名");
            return response;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            response.setData("token过期");
            return response;
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            response.setData("jwt算法不一致");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setData("未知错误");
            return response;
        }

        Map<String, Object> tokenInfo = jwtUtils.getTokenInfo(token);
        int role = Integer.parseInt((String) tokenInfo.get("role"));
        if (role < 1) {
            return new Result(false, StatusCode.ACCESS_ERROR, "权限不足，需要正式成员及以上的权限才可进行此操作");
        }
        return proceedingJoinPoint.proceed(args);
    }
}