package com.zaiqi.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.review.entity.ProfileReviewRecord;
import com.zaiqi.review.mapper.ProfileReviewRecordMapper;
import com.zaiqi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ProfileReviewRecordMapper reviewRecordMapper;

    @Override
    public void createReviewRecord(Long userId, String fieldName, String oldValue, String newValue) {
        ProfileReviewRecord record = new ProfileReviewRecord();
        record.setUserId(userId);
        record.setFieldName(fieldName);
        record.setOldValue(oldValue);
        record.setNewValue(newValue);
        record.setReviewStatus(0);
        reviewRecordMapper.insert(record);
    }

    @Override
    public PageResult<ProfileReviewRecord> getPendingReviews(int page, int size) {
        Page<ProfileReviewRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProfileReviewRecord> wrapper = new LambdaQueryWrapper<ProfileReviewRecord>()
                .eq(ProfileReviewRecord::getReviewStatus, 0)
                .orderByDesc(ProfileReviewRecord::getCreateTime);
        Page<ProfileReviewRecord> result = reviewRecordMapper.selectPage(pageParam, wrapper);
        return PageResult.of(result);
    }

    @Override
    @Transactional
    public void approveReview(Long recordId, Long reviewerId) {
        ProfileReviewRecord record = reviewRecordMapper.selectById(recordId);
        if (record == null) return;
        record.setReviewStatus(1);
        record.setReviewerId(reviewerId);
        record.setReviewTime(LocalDateTime.now());
        reviewRecordMapper.updateById(record);
    }

    @Override
    @Transactional
    public void rejectReview(Long recordId, Long reviewerId, String comment) {
        ProfileReviewRecord record = reviewRecordMapper.selectById(recordId);
        if (record == null) return;
        record.setReviewStatus(2);
        record.setReviewerId(reviewerId);
        record.setReviewComment(comment);
        record.setReviewTime(LocalDateTime.now());
        reviewRecordMapper.updateById(record);
    }

    @Override
    public void markRiskFlag(Long targetUserId, Long adminId, String riskFlag) {
        // 风险标记 - 在管理后台 Controller 中实现
    }
}
