package com.example.post.controller;


import com.example.common.entity.Result;
import com.example.post.dto.NewPost;
import com.example.post.dto.PagingParam;
import com.example.post.dto.SearchParam;
import com.example.post.service.IPostService;
import com.example.security.aspect.UserRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 帖子控制器
 */
@RestController
@RequestMapping("/post")
public class PostController {

    private IPostService postService;

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    //帖子发布请求
    @UserRequired
    @PostMapping
    public Result publishPost(@RequestHeader String token, @Valid @RequestBody NewPost newPost) {
        return postService.publishPost(token, newPost);
    }

    //删除帖子请求
    @UserRequired
    @DeleteMapping("/{postId}")
    public Result deletePost(@RequestHeader String token, @PathVariable Long postId) {
        return postService.deletePost(token, postId);
    }

    //获取帖子的具体内容
    @GetMapping("/{postId}")
    public Result getPostDetail(@RequestHeader String token, @PathVariable Long postId, @Valid PagingParam pagingParam) {
        return postService.getPostDetail(token, postId, pagingParam);
    }

    //获取帖子列表
    @GetMapping("/posts")
    public Result getPostList(@RequestHeader String token, SearchParam searchParam, @Valid PagingParam pagingParam) {
        return postService.getPostList(token, searchParam, pagingParam);
    }

    //是否赞过帖子
    @UserRequired
    @GetMapping("/liked/{postId}")
    public Result likedThePost(@RequestHeader String token, @PathVariable Long postId) {
        return postService.likedThePost(token, postId);
    }

    //给帖子点赞
    @UserRequired
    @PostMapping("/like/{postId}")
    public Result likeThePost(@RequestHeader String token, @PathVariable Long postId) {
        return postService.likeThePost(token, postId);
    }

    //帖子取消赞
    @UserRequired
    @DeleteMapping("/like/{postId}")
    public Result dislikeThePost(@RequestHeader String token, @PathVariable Long postId) {
        return postService.dislikeThePost(token, postId);
    }

}

