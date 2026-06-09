package com.zaiqi.user.service.impl;

import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.user.dto.PrivacySettingsRequest;
import com.zaiqi.user.dto.UserBasicInfoResponse;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import com.zaiqi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public UserBasicInfoResponse getBasicInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        String online = redisTemplate.opsForValue().get("online:user:" + userId);

        return UserBasicInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthYear(user.getBirthYear())
                .city(user.getCity())
                .level(user.getLevel())
                .onlineStatus("online".equals(online) ? 1 : 0)
                .showOnline(user.getShowOnline() == 1)
                .build();
    }

    @Override
    public void updateBasicInfo(Long userId, String nickname, Integer gender, Integer birthYear, String city) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (nickname != null) user.setNickname(nickname);
        if (gender != null) user.setGender(gender);
        if (birthYear != null) user.setBirthYear(birthYear);
        if (city != null) user.setCity(city);
        userMapper.updateById(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setAvatar(avatar);
        userMapper.updateById(user);
    }

    @Override
    public void setOnlineStatus(Long userId, boolean online) {
        String key = "online:user:" + userId;
        if (online) {
            redisTemplate.opsForValue().set(key, "online", Duration.ofMinutes(5));
        } else {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void updatePrivacy(Long userId, PrivacySettingsRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (request.getShowOnline() != null) {
            user.setShowOnline(request.getShowOnline());
        }
        userMapper.updateById(user);
    }
}
