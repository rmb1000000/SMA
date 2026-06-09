package com.zaiqi.auth.controller;

import com.zaiqi.auth.dto.LoginResponse;
import com.zaiqi.auth.dto.SmsLoginRequest;
import com.zaiqi.auth.dto.WxLoginRequest;
import com.zaiqi.auth.service.AuthService;
import com.zaiqi.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/wx-login")
    public Result<LoginResponse> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        LoginResponse response = authService.wxLogin(request.getCode(), request.getDeviceInfo());
        return Result.success(response);
    }

    @PostMapping("/sms-login")
    public Result<LoginResponse> smsLogin(@Valid @RequestBody SmsLoginRequest request) {
        LoginResponse response = authService.smsLogin(request.getPhone(), request.getSmsCode(), request.getDeviceInfo());
        return Result.success(response);
    }

    @PostMapping("/send-sms-code")
    public Result<Void> sendSmsCode(@RequestParam String phone) {
        // 模拟发送短信验证码（实际接入短信服务商 API）
        String code = "123456";
        redisTemplate.opsForValue().set("sms:code:" + phone, code, 5, TimeUnit.MINUTES);
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute Long userId,
                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(userId, token);
        return Result.success();
    }
}
