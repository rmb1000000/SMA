package com.zaiqi.post.service;

import com.zaiqi.post.entity.Post;
import com.zaiqi.post.entity.PostComment;
import java.util.List;

public interface PostService {
    Post createPost(Long userId, String content, String images, Integer visibility);
    void deletePost(Long postId, Long userId);
    List<Post> getPostFeed(int page, int size, Long viewerId);
    void likePost(Long postId, Long userId);
    void unlikePost(Long postId, Long userId);
    PostComment addComment(Long postId, Long userId, String content);
    void deleteComment(Long commentId, Long userId);
    List<PostComment> getComments(Long postId);
}
