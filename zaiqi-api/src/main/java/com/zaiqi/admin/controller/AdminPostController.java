package com.zaiqi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import com.zaiqi.post.entity.Post;
import com.zaiqi.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostMapper postMapper;

    @GetMapping
    public Result<PageResult<Post>> listPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Post> p = new Page<>(page, size);
        LambdaQueryWrapper<Post> w = new LambdaQueryWrapper<Post>().orderByDesc(Post::getCreateTime);
        return Result.success(PageResult.of(postMapper.selectPage(p, w)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        Post post = postMapper.selectById(id);
        if (post != null) { post.setStatus(0); postMapper.updateById(post); }
        return Result.success();
    }
}
