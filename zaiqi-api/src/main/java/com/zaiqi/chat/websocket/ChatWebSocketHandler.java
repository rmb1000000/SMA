package com.zaiqi.chat.websocket;

import com.zaiqi.auth.jwt.JwtTokenProvider;
import com.zaiqi.chat.service.ChatService;
import com.zaiqi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;
    private final MemberService memberService;

    private final Map<Long, WebSocketSession> onlineSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String token = extractToken(session);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId == null || !memberService.hasPermission(userId, "chat")) {
            closeSession(session);
            return;
        }
        onlineSessions.put(userId, session);
        log.info("用户 {} 已连接 WebSocket", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // WebSocket 仅用于实时推送，消息发送走 REST API
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        onlineSessions.entrySet().removeIf(e -> e.getValue().equals(session));
    }

    public void sendToUser(Long userId, String payload) {
        WebSocketSession session = onlineSessions.get(userId);
        if (session != null && session.isOpen()) {
            try { session.sendMessage(new TextMessage(payload)); }
            catch (IOException e) { log.error("发送 WebSocket 消息失败", e); }
        }
    }

    private String extractToken(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri != null && uri.getQuery() != null && uri.getQuery().startsWith("token=")) {
            return uri.getQuery().substring(6);
        }
        return null;
    }

    private void closeSession(WebSocketSession session) {
        try { session.close(); } catch (IOException ignored) {}
    }
}
