package com.zaiqi.feedback.service;

import com.zaiqi.common.PageResult;
import com.zaiqi.feedback.entity.Feedback;

public interface FeedbackService {
    void submitFeedback(Long userId, String content, String contact);
    PageResult<Feedback> getFeedbackList(int page, int size, Integer status);
    void replyFeedback(Long feedbackId, Long adminId, String reply);
}
