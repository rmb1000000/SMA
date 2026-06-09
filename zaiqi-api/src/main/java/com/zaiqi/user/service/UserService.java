package com.zaiqi.user.service;

import com.zaiqi.user.dto.PrivacySettingsRequest;
import com.zaiqi.user.dto.UserBasicInfoResponse;

public interface UserService {
    UserBasicInfoResponse getBasicInfo(Long userId);
    void updateBasicInfo(Long userId, String nickname, Integer gender, Integer birthYear, String city);
    void updateAvatar(Long userId, String avatar);
    void setOnlineStatus(Long userId, boolean online);
    void updatePrivacy(Long userId, PrivacySettingsRequest request);
}
