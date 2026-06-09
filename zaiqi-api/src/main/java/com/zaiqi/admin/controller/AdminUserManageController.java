package com.zaiqi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserManageController {

    private final UserMapper userMapper;

    @GetMapping
    public Result<PageResult<User>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        Page<User> p = new Page<>(page, size);
        LambdaQueryWrapper<User> w = new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime);
        if (keyword != null && !keyword.isEmpty()) {
            w.and(q -> q.like(User::getNickname, keyword).or().like(User::getId, keyword));
        }
        return Result.success(PageResult.of(userMapper.selectPage(p, w)));
    }

    @GetMapping("/{userId}")
    public Result<User> getUserDetail(@PathVariable Long userId) {
        return Result.success(userMapper.selectById(userId));
    }

    @PutMapping("/{userId}/status")
    public Result<Void> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        User user = userMapper.selectById(userId);
        if (user != null) { user.setStatus(status); userMapper.updateById(user); }
        return Result.success();
    }
}
