package com.zaiqi.admin.controller;

import com.zaiqi.common.Result;
import com.zaiqi.order.entity.Order;
import com.zaiqi.order.mapper.OrderMapper;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userMapper.selectCount(null);
        long todayNewUsers = userMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .apply("DATE(create_time) = CURDATE()"));
        long vipUsers = userMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getLevel, 1));
        long todayOrders = orderMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .apply("DATE(create_time) = CURDATE()"));
        long paidOrders = orderMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getStatus, 1));

        stats.put("totalUsers", totalUsers);
        stats.put("todayNewUsers", todayNewUsers);
        stats.put("vipUsers", vipUsers);
        stats.put("todayOrders", todayOrders);
        stats.put("paidOrders", paidOrders);

        return Result.success(stats);
    }
}
