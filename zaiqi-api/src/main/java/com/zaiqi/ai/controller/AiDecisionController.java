package com.zaiqi.ai.controller;

import com.zaiqi.ai.service.AiDecisionService;
import com.zaiqi.common.Result;
import com.zaiqi.member.annotation.RequireMemberLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiDecisionController {

    private final AiDecisionService aiDecisionService;

    @GetMapping("/readiness/questions")
    public Result<Map<String, Object>> getReadinessQuestions() {
        return Result.success(aiDecisionService.getReadinessQuestions());
    }

    @PostMapping("/readiness/submit")
    public Result<Map<String, Object>> submitReadiness(@RequestAttribute Long userId,
                                                        @RequestBody Map<String, Object> answers) {
        return Result.success(aiDecisionService.getReadinessAssessment(userId, answers));
    }

    @GetMapping("/match/{targetUserId}")
    public Result<Map<String, Object>> getMatchAnalysis(@RequestAttribute Long userId,
                                                         @PathVariable Long targetUserId) {
        return Result.success(aiDecisionService.getMatchAnalysis(userId, targetUserId));
    }

    @GetMapping("/report/{targetUserId}")
    @RequireMemberLevel(value = "report_full", message = "完整决策报告仅限高级会员")
    public Result<Map<String, Object>> getFullReport(@RequestAttribute Long userId,
                                                      @PathVariable Long targetUserId) {
        return Result.success(aiDecisionService.getFullReport(userId, targetUserId));
    }

    @GetMapping("/assist/{sessionId}")
    @RequireMemberLevel(value = "chat", message = "请升级会员后使用")
    public Result<Map<String, Object>> getChatAssist(@RequestAttribute Long userId,
                                                      @PathVariable Long sessionId) {
        return Result.success(aiDecisionService.getChatSuggestion(userId, sessionId));
    }
}
