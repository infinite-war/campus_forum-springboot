package com.example.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.Result;
import com.example.post.dto.NewComment;
import com.example.post.entity.Comment;

/**
 * 评论服务接口
 */
public interface ICommentService extends IService<Comment> {

    //发表评论
    Result publishComment(String token, NewComment newComment);

    //删除评论
    Result deleteComment(String token, Long commentId);

    //给评论点赞
    Result likeTheComment(String token, Long commentId);

    //给评论取消赞
    Result dislikeTheComment(String token, Long commentId);

}
