package com.example.post.dto;

import lombok.Data;

/**
 * 搜索参数
 */
@Data
public class SearchParam {

    //搜索关键字
    private String keyword;

    //用户ID
    private Long userId;

    //分区
    private Integer category;
}
