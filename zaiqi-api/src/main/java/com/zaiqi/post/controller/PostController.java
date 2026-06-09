package com.zaiqi.post.controller;

import com.zaiqi.common.Result;
import com.zaiqi.member.annotation.RequireMemberLevel;
import com.zaiqi.post.entity.Post;
import com.zaiqi.post.entity.PostComment;
import com.zaiqi.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @RequireMemberLevel(value = "post", message = "发布动态仅限高级会员")
    public Result<Post> createPost(@RequestAttribute Long userId,
                                    @RequestParam String content,
                                    @RequestParam(required = false) String images,
                                    @RequestParam(required = false) Integer visibility) {
        return Result.success(postService.createPost(userId, content, images, visibility));
    }

    @GetMapping
    public Result<List<Post>> getFeed(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestAttribute Long userId) {
        return Result.success(postService.getPostFeed(page, size, userId));
    }

    @PostMapping("/{postId}/like")
    public Result<Void> likePost(@RequestAttribute Long userId, @PathVariable Long postId) {
        postService.likePost(postId, userId);
        return Result.success();
    }

    @PostMapping("/{postId}/unlike")
    public Result<Void> unlikePost(@RequestAttribute Long userId, @PathVariable Long postId) {
        postService.unlikePost(postId, userId);
        return Result.success();
    }

    @PostMapping("/{postId}/comments")
    public Result<PostComment> addComment(@RequestAttribute Long userId,
                                           @PathVariable Long postId,
                                           @RequestParam String content) {
        return Result.success(postService.addComment(postId, userId, content));
    }

    @GetMapping("/{postId}/comments")
    public Result<List<PostComment>> getComments(@PathVariable Long postId) {
        return Result.success(postService.getComments(postId));
    }

    @DeleteMapping("/{postId}")
    public Result<Void> deletePost(@RequestAttribute Long userId, @PathVariable Long postId) {
        postService.deletePost(postId, userId);
        return Result.success();
    }

    @DeleteMapping("/comments/{commentId}")
    public Result<Void> deleteComment(@RequestAttribute Long userId, @PathVariable Long commentId) {
        postService.deleteComment(commentId, userId);
        return Result.success();
    }
}
