package com.zaiqi.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.user.dto.UserProfileRequest;
import com.zaiqi.user.dto.UserProfileResponse;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.entity.UserProfile;
import com.zaiqi.user.mapper.UserMapper;
import com.zaiqi.user.mapper.UserProfileMapper;
import com.zaiqi.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse getProfile(Long userId, Long viewerId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));
        boolean isSelf = userId.equals(viewerId);

        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthYear(user.getBirthYear())
                .city(user.getCity());

        if (profile != null) {
            builder
                .bio(profile.getBio())
                .photos(parseJsonArray(profile.getPhotos()))
                .maritalStatus(profile.getMaritalStatus())
                .hasChildren(profile.getHasChildren())
                .childrenCustody(profile.getChildrenCustody())
                .occupation(profile.getOccupation())
                .education(profile.getEducation())
                .annualIncome(profile.getAnnualIncome());

            if (isSelf) {
                builder.dimValues(profile.getDimValues())
                       .dimLifestyle(profile.getDimLifestyle())
                       .dimEconomy(profile.getDimEconomy())
                       .dimFamily(profile.getDimFamily())
                       .dimEmotion(profile.getDimEmotion());
            }

            builder.riskFlags(parseJsonArray(profile.getRiskFlags()))
                   .riskAutoTags(parseJsonArray(profile.getRiskAutoTags()));
        }

        return builder.build();
    }

    @Override
    @Transactional
    public void saveOrUpdateProfile(Long userId, UserProfileRequest request) {
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, userId));

        boolean isNew = profile == null;
        if (isNew) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }

        profile.setBio(request.getBio());
        profile.setPhotos(request.getPhotos());
        profile.setMaritalStatus(request.getMaritalStatus());
        profile.setHasChildren(request.getHasChildren());
        profile.setChildrenCustody(request.getChildrenCustody());
        profile.setOccupation(request.getOccupation());
        profile.setEducation(request.getEducation());
        profile.setAnnualIncome(request.getAnnualIncome());
        profile.setDimValues(request.getDimValues());
        profile.setDimLifestyle(request.getDimLifestyle());
        profile.setDimEconomy(request.getDimEconomy());
        profile.setDimFamily(request.getDimFamily());
        profile.setDimEmotion(request.getDimEmotion());
        profile.setProfileStatus(2);

        if (isNew) {
            userProfileMapper.insert(profile);
        } else {
            userProfileMapper.updateById(profile);
        }
    }

    private List<String> parseJsonArray(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, String.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
