package com.zaiqi.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.feedback.entity.Feedback;
import com.zaiqi.feedback.mapper.FeedbackMapper;
import com.zaiqi.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackMapper feedbackMapper;

    @Override
    public void submitFeedback(Long userId, String content, String contact) {
        Feedback fb = new Feedback();
        fb.setUserId(userId); fb.setContent(content); fb.setContact(contact);
        fb.setStatus(0);
        feedbackMapper.insert(fb);
    }

    @Override
    public PageResult<Feedback> getFeedbackList(int page, int size, Integer status) {
        Page<Feedback> p = new Page<>(page, size);
        LambdaQueryWrapper<Feedback> w = new LambdaQueryWrapper<Feedback>().orderByDesc(Feedback::getCreateTime);
        if (status != null) w.eq(Feedback::getStatus, status);
        return PageResult.of(feedbackMapper.selectPage(p, w));
    }

    @Override
    @Transactional
    public void replyFeedback(Long feedbackId, Long adminId, String reply) {
        Feedback fb = feedbackMapper.selectById(feedbackId);
        if (fb != null) {
            fb.setStatus(1); fb.setReply(reply); fb.setHandlerId(adminId);
            fb.setHandleTime(LocalDateTime.now());
            feedbackMapper.updateById(fb);
        }
    }
}
