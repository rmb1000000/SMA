package com.zaiqi.auth.service;

import com.zaiqi.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse wxLogin(String code, String deviceInfo);

    LoginResponse smsLogin(String phone, String smsCode, String deviceInfo);

    LoginResponse refreshToken(String refreshToken);

    void logout(Long userId, String accessToken);
}
