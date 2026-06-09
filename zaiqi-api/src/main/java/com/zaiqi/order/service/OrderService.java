package com.zaiqi.order.service;

import com.zaiqi.order.dto.CreateOrderRequest;
import com.zaiqi.order.dto.CreateOrderResponse;
import com.zaiqi.order.entity.Order;

public interface OrderService {
    CreateOrderResponse createOrder(Long userId, CreateOrderRequest request);
    Order getOrderByOrderNo(String orderNo);
    void handlePaymentSuccess(String orderNo, String wxTransactionId, String notifyRaw);
}
