package com.zaiqi.feedback.controller;

import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import com.zaiqi.feedback.entity.Feedback;
import com.zaiqi.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/feedbacks")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public Result<Void> submit(@RequestAttribute Long userId,
                                @RequestParam String content,
                                @RequestParam(required = false) String contact) {
        feedbackService.submitFeedback(userId, content, contact);
        return Result.success();
    }

    @GetMapping
    public Result<PageResult<Feedback>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status) {
        return Result.success(feedbackService.getFeedbackList(page, size, status));
    }

    @PostMapping("/{id}/reply")
    public Result<Void> reply(@RequestAttribute Long userId,
                               @PathVariable Long id,
                               @RequestParam String reply) {
        feedbackService.replyFeedback(id, userId, reply);
        return Result.success();
    }
}
