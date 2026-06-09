package com.zaiqi.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.recommend.dto.RecommendUserDTO;
import com.zaiqi.recommend.service.RecommendService;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.entity.UserProfile;
import com.zaiqi.user.mapper.UserMapper;
import com.zaiqi.user.mapper.UserProfileMapper;
import com.zaiqi.verification.entity.UserVerification;
import com.zaiqi.verification.mapper.UserVerificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserVerificationMapper verificationMapper;
    private final StringRedisTemplate redisTemplate;

    private static final int DAILY_LIMIT_BASIC = 20;

    @Override
    public List<RecommendUserDTO> getRecommendations(Long userId, int count) {
        User currentUser = userMapper.selectById(userId);
        if (currentUser == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (currentUser.getLevel() == 0) {
            String dailyKey = "recommend:daily:" + userId + ":" + LocalDate.now();
            String usedStr = redisTemplate.opsForValue().get(dailyKey);
            int used = usedStr != null ? Integer.parseInt(usedStr) : 0;
            if (used >= DAILY_LIMIT_BASIC) {
                throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "每日推荐次数已用完");
            }
        }

        String excludedKey = "recommend:excluded:" + userId;
        Set<String> excludedSet = redisTemplate.opsForSet().members(excludedKey);
        Set<Long> excludedIds = excludedSet != null
                ? excludedSet.stream().map(Long::parseLong).collect(Collectors.toSet())
                : new HashSet<>();
        excludedIds.add(userId);

        int targetGender = currentUser.getGender() == 1 ? 2 : 1;
        List<User> candidates = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getGender, targetGender)
                        .eq(User::getStatus, 1)
                        .notIn(!excludedIds.isEmpty(), User::getId, excludedIds)
                        .last("LIMIT 200"));

        List<RecommendUserDTO> result = new ArrayList<>();
        for (User candidate : candidates) {
            UserProfile profile = userProfileMapper.selectOne(
                    new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, candidate.getId()));

            int matchScore = calculateMatchScore(currentUser, profile);
            int behaviorScore = calculateBehaviorScore(candidate, profile);
            int finalScore = (int) (matchScore * 0.6 + behaviorScore * 0.4);

            boolean verified = verificationMapper.selectOne(
                    new LambdaQueryWrapper<UserVerification>()
                            .eq(UserVerification::getUserId, candidate.getId())
                            .eq(UserVerification::getVerifyType, 2)
                            .eq(UserVerification::getVerifyStatus, 1)) != null;

            List<String> riskFlags = new ArrayList<>();
            List<String> riskAutoTags = new ArrayList<>();
            int photosCount = 0;
            if (profile != null) {
                if (profile.getRiskFlags() != null) riskFlags = parseJsonList(profile.getRiskFlags());
                if (profile.getRiskAutoTags() != null) riskAutoTags = parseJsonList(profile.getRiskAutoTags());
                if (profile.getPhotos() != null) photosCount = parseJsonList(profile.getPhotos()).size();
            }

            result.add(RecommendUserDTO.builder()
                    .userId(candidate.getId()).nickname(candidate.getNickname())
                    .avatar(candidate.getAvatar()).gender(candidate.getGender())
                    .birthYear(candidate.getBirthYear()).city(candidate.getCity())
                    .bio(profile != null ? profile.getBio() : null)
                    .matchScore(finalScore).riskFlags(riskFlags)
                    .riskAutoTags(riskAutoTags).identityVerified(verified)
                    .photosCount(photosCount).build());
        }

        result.sort((a, b) -> b.getMatchScore() - a.getMatchScore());
        List<RecommendUserDTO> topResults = result.stream().limit(count).toList();

        for (RecommendUserDTO dto : topResults) {
            redisTemplate.opsForSet().add(excludedKey, String.valueOf(dto.getUserId()));
        }
        redisTemplate.expire(excludedKey, 30, TimeUnit.DAYS);

        if (currentUser.getLevel() == 0) {
            String dailyKey = "recommend:daily:" + userId + ":" + LocalDate.now();
            redisTemplate.opsForValue().increment(dailyKey);
            redisTemplate.expire(dailyKey, 1, TimeUnit.DAYS);
        }

        return topResults;
    }

    @Override
    public void markNotInterested(Long userId, Long targetUserId) {
        String key = "recommend:excluded:" + userId;
        redisTemplate.opsForSet().add(key, String.valueOf(targetUserId));
        redisTemplate.expire(key, 30, TimeUnit.DAYS);
    }

    @Override
    public void recordProfileView(Long viewerId, Long targetUserId) {
        String key = "profile_views:" + targetUserId;
        redisTemplate.opsForSet().add(key, String.valueOf(viewerId));
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    private int calculateMatchScore(User current, UserProfile targetProfile) {
        UserProfile currentProfile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>().eq(UserProfile::getUserId, current.getId()));
        if (currentProfile == null || targetProfile == null) return 50;

        int[] cur = {currentProfile.getDimValues(), currentProfile.getDimLifestyle(),
                     currentProfile.getDimEconomy(), currentProfile.getDimFamily(),
                     currentProfile.getDimEmotion()};
        int[] tgt = {targetProfile.getDimValues(), targetProfile.getDimLifestyle(),
                     targetProfile.getDimEconomy(), targetProfile.getDimFamily(),
                     targetProfile.getDimEmotion()};

        int total = 0;
        for (int i = 0; i < 5; i++) {
            total += Math.max(0, 20 - Math.abs(cur[i] - tgt[i]));
        }
        return Math.min(100, total);
    }

    private int calculateBehaviorScore(User target, UserProfile profile) {
        int score = 0;
        if (profile != null) {
            int filled = 0;
            if (profile.getBio() != null && !profile.getBio().isEmpty()) filled++;
            if (profile.getOccupation() != null) filled++;
            if (profile.getEducation() != null && profile.getEducation() > 0) filled++;
            if (profile.getAnnualIncome() != null) filled++;
            if (profile.getMaritalStatus() != null && profile.getMaritalStatus() > 0) filled++;
            score += Math.min(10, filled * 2);
        }

        String onlineKey = "online:user:" + target.getId();
        if (redisTemplate.opsForValue().get(onlineKey) != null) score += 15;

        UserVerification ver = verificationMapper.selectOne(
                new LambdaQueryWrapper<UserVerification>()
                        .eq(UserVerification::getUserId, target.getId())
                        .eq(UserVerification::getVerifyType, 2)
                        .eq(UserVerification::getVerifyStatus, 1));
        if (ver != null) score += 10;

        Long viewCount = redisTemplate.opsForSet().size("profile_views:" + target.getId());
        score += Math.min(20, (viewCount != null ? viewCount.intValue() : 0) * 2);

        return Math.min(100, score);
    }

    private List<String> parseJsonList(String json) {
        try { return com.alibaba.fastjson2.JSON.parseArray(json, String.class); }
        catch (Exception e) { return Collections.emptyList(); }
    }
}
