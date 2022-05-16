package com.example.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 发表楼层参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFloor {

    //所属帖子ID
    private Long postId;

    //楼层内容
    @NotBlank
    @Length(max = 200)
    private String content;

}
