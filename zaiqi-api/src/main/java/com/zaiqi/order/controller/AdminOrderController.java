package com.zaiqi.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import com.zaiqi.member.service.MemberService;
import com.zaiqi.order.entity.Order;
import com.zaiqi.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderMapper orderMapper;
    private final MemberService memberService;

    @GetMapping
    public Result<PageResult<Order>> listOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .orderByDesc(Order::getCreateTime);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        return Result.success(PageResult.of(orderMapper.selectPage(pageParam, wrapper)));
    }

    @PutMapping("/{userId}/disable")
    public Result<Void> disableMember(@RequestAttribute Long userId,
                                       @PathVariable Long targetUserId) {
        memberService.disableMember(targetUserId, userId);
        return Result.success();
    }

    @PutMapping("/{userId}/enable")
    public Result<Void> enableMember(@RequestAttribute Long userId,
                                      @PathVariable Long targetUserId) {
        memberService.enableMember(targetUserId, userId);
        return Result.success();
    }
}
