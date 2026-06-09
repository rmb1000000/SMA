package com.zaiqi.chat.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.chat.entity.ChatMessage;
import com.zaiqi.chat.entity.ChatSession;
import com.zaiqi.chat.mapper.ChatMessageMapper;
import com.zaiqi.chat.mapper.ChatSessionMapper;
import com.zaiqi.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;

    @Override
    @Transactional
    public ChatSession createOrGetSession(Long userA, Long userB) {
        Long min = Math.min(userA, userB);
        Long max = Math.max(userA, userB);
        ChatSession existing = sessionMapper.selectOne(
                new LambdaQueryWrapper<ChatSession>()
                        .eq(ChatSession::getUserA, min)
                        .eq(ChatSession::getUserB, max));
        if (existing != null) return existing;

        ChatSession session = new ChatSession();
        session.setUserA(min);
        session.setUserB(max);
        session.setStatus(0);
        sessionMapper.insert(session);
        return session;
    }

    @Override
    public List<ChatSession> getUserSessions(Long userId) {
        return sessionMapper.selectList(
                new LambdaQueryWrapper<ChatSession>()
                        .and(w -> w.eq(ChatSession::getUserA, userId)
                                  .or().eq(ChatSession::getUserB, userId))
                        .orderByDesc(ChatSession::getLastMessageTime));
    }

    @Override
    public List<ChatMessage> getMessages(Long sessionId, int page, int size) {
        Page<ChatMessage> p = new Page<>(page, size);
        return messageMapper.selectPage(p,
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .orderByDesc(ChatMessage::getCreateTime)).getRecords();
    }

    @Override
    @Transactional
    public ChatMessage sendMessage(Long sessionId, Long senderId, String content, Integer msgType) {
        ChatMessage msg = new ChatMessage();
        msg.setSessionId(sessionId);
        msg.setSenderId(senderId);
        msg.setContent(content);
        msg.setMsgType(msgType != null ? msgType : 1);
        msg.setStatus(1);
        msg.setCreateTime(LocalDateTime.now());
        messageMapper.insert(msg);

        ChatSession session = sessionMapper.selectById(sessionId);
        if (session != null) {
            session.setLastMessage(content);
            session.setLastMessageTime(LocalDateTime.now());
            sessionMapper.updateById(session);
        }
        return msg;
    }

    @Override
    @Transactional
    public boolean recallMessage(Long messageId, Long userId) {
        ChatMessage msg = messageMapper.selectById(messageId);
        if (msg == null || !msg.getSenderId().equals(userId)) return false;
        if (msg.getCreateTime().plusMinutes(2).isBefore(LocalDateTime.now())) return false;

        msg.setStatus(2);
        messageMapper.updateById(msg);
        return true;
    }
}
