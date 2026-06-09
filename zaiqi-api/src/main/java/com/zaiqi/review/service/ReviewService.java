package com.zaiqi.review.service;

import com.zaiqi.common.PageResult;
import com.zaiqi.review.entity.ProfileReviewRecord;

public interface ReviewService {
    void createReviewRecord(Long userId, String fieldName, String oldValue, String newValue);
    PageResult<ProfileReviewRecord> getPendingReviews(int page, int size);
    void approveReview(Long recordId, Long reviewerId);
    void rejectReview(Long recordId, Long reviewerId, String comment);
    void markRiskFlag(Long targetUserId, Long adminId, String riskFlag);
}
