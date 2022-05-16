package com.example.post.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 分页参数
 */
@Data
public class PagingParam {

    //单页数量
    @Min(1)
    private Integer size;

    //当前页数
    @Min(1)
    private Integer page;

    //显示顺序
    private Integer order;

}
