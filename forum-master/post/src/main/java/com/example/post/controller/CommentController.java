package com.example.post.controller;


import com.example.common.entity.Result;
import com.example.post.dto.NewComment;
import com.example.post.service.ICommentService;
import com.example.security.aspect.UserRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/post/comment")
public class CommentController {

    private ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    //评论发布请求
    @UserRequired
    @PostMapping
    public Result publishComment(@RequestHeader String token, @Valid @RequestBody NewComment newComment) {
        return commentService.publishComment(token, newComment);
    }

    //评论删除请求
    @UserRequired
    @DeleteMapping("/{commentId}")
    public Result deleteComment(@RequestHeader String token, @PathVariable Long commentId) {
        return commentService.deleteComment(token, commentId);
    }

    //评论点赞请求
    @UserRequired
    @PostMapping("/like/{commentId}")
    public Result likeTheComment(@RequestHeader String token, @PathVariable Long commentId) {
        return commentService.likeTheComment(token, commentId);
    }

    //评论取消赞请求
    @UserRequired
    @DeleteMapping("/like/{commentId}")
    public Result dislikeTheComment(@RequestHeader String token, @PathVariable Long commentId) {
        return commentService.dislikeTheComment(token, commentId);
    }

}

