package com.zaiqi.ai.service;

import java.util.Map;

public interface AiDecisionService {
    Map<String, Object> getMatchAnalysis(Long userId, Long targetUserId);
    Map<String, Object> getFullReport(Long userId, Long targetUserId);
    Map<String, Object> getReadinessAssessment(Long userId, Map<String, Object> answers);
    Map<String, Object> getChatSuggestion(Long userId, Long sessionId);
    Map<String, Object> getReadinessQuestions();
}
