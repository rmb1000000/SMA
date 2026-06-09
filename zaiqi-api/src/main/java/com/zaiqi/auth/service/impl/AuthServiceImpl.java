package com.zaiqi.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.auth.dto.LoginResponse;
import com.zaiqi.auth.entity.LoginRecord;
import com.zaiqi.auth.entity.UserAuth;
import com.zaiqi.auth.jwt.JwtTokenProvider;
import com.zaiqi.auth.mapper.LoginRecordMapper;
import com.zaiqi.auth.mapper.UserAuthMapper;
import com.zaiqi.auth.service.AuthService;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAuthMapper userAuthMapper;
    private final UserMapper userMapper;
    private final LoginRecordMapper loginRecordMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public LoginResponse wxLogin(String code, String deviceInfo) {
        String mockPhone = "138" + RandomUtil.randomNumbers(8);

        UserAuth userAuth = userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>().eq(UserAuth::getPhone, mockPhone));

        Long userId;
        if (userAuth == null) {
            User user = new User();
            user.setNickname("用户" + RandomUtil.randomString(6));
            user.setStatus(1);
            user.setLevel(0);
            user.setFreeReportCount(5);
            user.setShowOnline(1);
            userMapper.insert(user);
            userId = user.getId();

            UserAuth newAuth = new UserAuth();
            newAuth.setUserId(userId);
            newAuth.setPhone(mockPhone);
            newAuth.setPhoneVerified(1);
            newAuth.setWxOpenid("mock_openid_" + code);
            userAuthMapper.insert(newAuth);
        } else {
            userId = userAuth.getUserId();
        }

        saveLoginRecord(userId, 1, deviceInfo);
        redisTemplate.opsForValue().set("online:user:" + userId, "online", 5, TimeUnit.MINUTES);

        String accessToken = jwtTokenProvider.generateAccessToken(userId, "USER");
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        User user = userMapper.selectById(userId);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .nickname(user.getNickname())
                .level(user.getLevel())
                .profileCompleted(user.getNickname() != null && user.getGender() != null && user.getGender() != 0)
                .build();
    }

    @Override
    @Transactional
    public LoginResponse smsLogin(String phone, String smsCode, String deviceInfo) {
        String cacheCode = redisTemplate.opsForValue().get("sms:code:" + phone);
        if (cacheCode == null || !cacheCode.equals(smsCode)) {
            throw new BusinessException(ErrorCode.SMS_CODE_ERROR);
        }
        redisTemplate.delete("sms:code:" + phone);

        UserAuth userAuth = userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>().eq(UserAuth::getPhone, phone));

        Long userId;
        if (userAuth == null) {
            User user = new User();
            user.setNickname("用户" + RandomUtil.randomString(6));
            user.setStatus(1);
            user.setLevel(0);
            user.setFreeReportCount(5);
            user.setShowOnline(1);
            userMapper.insert(user);
            userId = user.getId();

            UserAuth newAuth = new UserAuth();
            newAuth.setUserId(userId);
            newAuth.setPhone(phone);
            newAuth.setPhoneVerified(1);
            userAuthMapper.insert(newAuth);
        } else {
            userId = userAuth.getUserId();
            User user = userMapper.selectById(userId);
            if (user == null || user.getStatus() == 0) {
                throw new BusinessException(ErrorCode.USER_DISABLED);
            }
        }

        saveLoginRecord(userId, 2, deviceInfo);
        redisTemplate.opsForValue().set("online:user:" + userId, "online", 5, TimeUnit.MINUTES);

        String accessToken = jwtTokenProvider.generateAccessToken(userId, "USER");
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        User user = userMapper.selectById(userId);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .nickname(user.getNickname())
                .level(user.getLevel())
                .profileCompleted(user.getNickname() != null && user.getGender() != null && user.getGender() != 0)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        if (userId == null) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, "USER");
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        User user = userMapper.selectById(userId);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(userId)
                .nickname(user.getNickname())
                .level(user.getLevel())
                .profileCompleted(true)
                .build();
    }

    @Override
    public void logout(Long userId, String accessToken) {
        redisTemplate.opsForValue().set("blacklist:token:" + accessToken, "1", 24, TimeUnit.HOURS);
        redisTemplate.delete("online:user:" + userId);
    }

    private void saveLoginRecord(Long userId, int loginType, String deviceInfo) {
        LoginRecord record = new LoginRecord();
        record.setUserId(userId);
        record.setLoginType(loginType);
        record.setDeviceInfo(deviceInfo);
        record.setCreateTime(LocalDateTime.now());
        loginRecordMapper.insert(record);
    }
}
