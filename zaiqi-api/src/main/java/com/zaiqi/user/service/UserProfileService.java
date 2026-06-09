package com.zaiqi.user.service;

import com.zaiqi.user.dto.UserProfileRequest;
import com.zaiqi.user.dto.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getProfile(Long userId, Long viewerId);
    void saveOrUpdateProfile(Long userId, UserProfileRequest request);
}
