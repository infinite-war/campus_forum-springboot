package com.example.common.entity;

/**
 * 状态码
 */
public class StatusCode {

    //成功
    public static final int OK = 20000;

    //失败
    public static final int ERROR = 20001;

    //用户名或密码错误
    public static final int LOGIN_ERROR = 20002;

    //远程调用失败
    public static final int REMOTE_ERROR = 20003;

    //重复操作
    public static final int REP_ERROR = 20004;

    //参数校验错误
    public static final int PARAM_ERROR = 400;

    //权限不足
    public static final int ACCESS_ERROR = 401;

    //未找到
    public static final int NOT_FOUND = 404;

}