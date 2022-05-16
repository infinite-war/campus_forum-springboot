package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结果集模板
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    //是否成功
    private Boolean flag;

    //状态码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private Object data;

    public Result(Boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
}
