package com.zaiqi.order.controller;

import com.zaiqi.common.Result;
import com.zaiqi.order.dto.CreateOrderRequest;
import com.zaiqi.order.dto.CreateOrderResponse;
import com.zaiqi.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<CreateOrderResponse> createOrder(@RequestAttribute Long userId,
                                                    @Valid @RequestBody CreateOrderRequest request) {
        return Result.success(orderService.createOrder(userId, request));
    }

    @GetMapping("/{orderNo}")
    public Result<?> getOrder(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByOrderNo(orderNo));
    }
}
