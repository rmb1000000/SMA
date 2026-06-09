package com.zaiqi.order.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.member.service.MemberService;
import com.zaiqi.order.dto.CreateOrderRequest;
import com.zaiqi.order.dto.CreateOrderResponse;
import com.zaiqi.order.entity.Order;
import com.zaiqi.order.mapper.OrderMapper;
import com.zaiqi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final MemberService memberService;

    private static final Map<Integer, Object[]> PRODUCTS = Map.of(
        1, new Object[]{"月度高级会员", 6900},
        2, new Object[]{"季度高级会员", 18900},
        3, new Object[]{"年度高级会员", 28900},
        4, new Object[]{"AI单次分析", 990},
        5, new Object[]{"决策报告超额购买", 990}
    );

    @Override
    @Transactional
    public CreateOrderResponse createOrder(Long userId, CreateOrderRequest request) {
        Object[] product = PRODUCTS.get(request.getProductType());
        if (product == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "无效的商品类型");
        }

        String productName = (String) product[0];
        int amount = (int) product[1];
        String orderNo = generateOrderNo();

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setProductType(request.getProductType());
        order.setProductName(productName);
        order.setAmount(amount);
        order.setCurrency("CNY");
        order.setStatus(0);
        orderMapper.insert(order);

        String prepayId = "mock_prepay_id_" + RandomUtil.randomString(24);
        String nonceStr = RandomUtil.randomString(32);
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        return CreateOrderResponse.builder()
                .orderNo(orderNo)
                .amount(amount)
                .productName(productName)
                .prepayId(prepayId)
                .nonceStr(nonceStr)
                .timeStamp(timeStamp)
                .signType("RSA")
                .paySign("mock_pay_sign")
                .build();
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return orderMapper.selectOne(
                new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(String orderNo, String wxTransactionId, String notifyRaw) {
        Order order = getOrderByOrderNo(orderNo);
        if (order == null || order.getStatus() != 0) {
            log.warn("订单状态异常: orderNo={}", orderNo);
            return;
        }

        order.setStatus(1);
        order.setWxTransactionId(wxTransactionId);
        order.setWxNotifyRaw(notifyRaw);
        order.setPaidTime(LocalDateTime.now());
        orderMapper.updateById(order);

        memberService.handleProductPurchase(order.getUserId(), order.getProductType(), order.getId());
    }

    private String generateOrderNo() {
        return "ZQ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
               + RandomUtil.randomNumbers(6);
    }
}
