package com.zaiqi.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.post.entity.Post;
import com.zaiqi.post.entity.PostComment;
import com.zaiqi.post.entity.PostLike;
import com.zaiqi.post.mapper.PostCommentMapper;
import com.zaiqi.post.mapper.PostLikeMapper;
import com.zaiqi.post.mapper.PostMapper;
import com.zaiqi.post.service.PostService;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;
    private final PostCommentMapper postCommentMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Post createPost(Long userId, String content, String images, Integer visibility) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        post.setImages(images);
        post.setVisibility(visibility != null ? visibility : 1);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1);
        postMapper.insert(post);
        return post;
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postMapper.selectById(postId);
        if (post != null && post.getUserId().equals(userId)) {
            post.setStatus(0);
            postMapper.updateById(post);
        }
    }

    @Override
    public List<Post> getPostFeed(int page, int size, Long viewerId) {
        User viewer = userMapper.selectById(viewerId);
        Page<Post> p = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, 1)
                .orderByDesc(Post::getCreateTime);
        if (viewer != null && viewer.getLevel() == 0) {
            wrapper.ne(Post::getVisibility, 2);
        }
        return postMapper.selectPage(p, wrapper).getRecords();
    }

    @Override
    @Transactional
    public void likePost(Long postId, Long userId) {
        PostLike existing = postLikeMapper.selectOne(
                new LambdaQueryWrapper<PostLike>()
                        .eq(PostLike::getPostId, postId)
                        .eq(PostLike::getUserId, userId));
        if (existing != null) return;

        PostLike like = new PostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        postLikeMapper.insert(like);

        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        postLikeMapper.delete(
                new LambdaQueryWrapper<PostLike>()
                        .eq(PostLike::getPostId, postId)
                        .eq(PostLike::getUserId, userId));

        Post post = postMapper.selectById(postId);
        if (post != null && post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public PostComment addComment(Long postId, Long userId, String content) {
        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setStatus(1);
        postCommentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        PostComment comment = postCommentMapper.selectById(commentId);
        if (comment == null) return;

        Post post = postMapper.selectById(comment.getPostId());
        if (post != null && (comment.getUserId().equals(userId) || post.getUserId().equals(userId))) {
            comment.setStatus(0);
            postCommentMapper.updateById(comment);
        }
    }

    @Override
    public List<PostComment> getComments(Long postId) {
        return postCommentMapper.selectList(
                new LambdaQueryWrapper<PostComment>()
                        .eq(PostComment::getPostId, postId)
                        .eq(PostComment::getStatus, 1)
                        .orderByAsc(PostComment::getCreateTime));
    }
}
