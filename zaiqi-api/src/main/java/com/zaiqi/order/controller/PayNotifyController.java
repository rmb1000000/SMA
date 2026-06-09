package com.zaiqi.order.controller;

import com.zaiqi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PayNotifyController {

    private final OrderService orderService;

    @PostMapping("/notify")
    public String handlePayNotify(@RequestBody String notifyBody) {
        log.info("微信支付回调: {}", notifyBody);
        try {
            // 实际项目: 解密 resource.ciphertext → 提取 orderNo → handlePaymentSuccess
            return "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";
        } catch (Exception e) {
            log.error("回调处理失败", e);
            return "{\"code\":\"FAIL\",\"message\":\"失败\"}";
        }
    }
}
