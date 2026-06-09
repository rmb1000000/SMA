package com.zaiqi.chat.service;

import com.zaiqi.chat.entity.ChatMessage;
import com.zaiqi.chat.entity.ChatSession;
import java.util.List;

public interface ChatService {
    ChatSession createOrGetSession(Long userA, Long userB);
    List<ChatSession> getUserSessions(Long userId);
    List<ChatMessage> getMessages(Long sessionId, int page, int size);
    ChatMessage sendMessage(Long sessionId, Long senderId, String content, Integer msgType);
    boolean recallMessage(Long messageId, Long userId);
}
