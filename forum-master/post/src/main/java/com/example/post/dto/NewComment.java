package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 发表评论参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewComment {

    //所属楼层ID
    private Long floorId;

    //评论内容
    @NotBlank
    @Length(max = 200)
    private String content;

}
