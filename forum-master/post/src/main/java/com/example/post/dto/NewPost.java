package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 发表帖子参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPost {

    //帖子分区
    private Integer category;

    //帖子标题
    @NotBlank
    @Length(max = 30)
    private String title;

    //帖子内容
    @NotBlank
    @Length() //default 2147483647
    private String content;

}
