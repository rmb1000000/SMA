package com.zaiqi.chat.controller;

import com.zaiqi.chat.entity.ChatMessage;
import com.zaiqi.chat.entity.ChatSession;
import com.zaiqi.chat.service.ChatService;
import com.zaiqi.common.Result;
import com.zaiqi.member.annotation.RequireMemberLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@RequireMemberLevel(value = "chat", message = "聊天功能仅限高级会员使用")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/session/{targetUserId}")
    public Result<ChatSession> createSession(@RequestAttribute Long userId,
                                              @PathVariable Long targetUserId) {
        return Result.success(chatService.createOrGetSession(userId, targetUserId));
    }

    @GetMapping("/sessions")
    public Result<List<ChatSession>> getSessions(@RequestAttribute Long userId) {
        return Result.success(chatService.getUserSessions(userId));
    }

    @GetMapping("/messages/{sessionId}")
    public Result<List<ChatMessage>> getMessages(@PathVariable Long sessionId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "50") int size) {
        return Result.success(chatService.getMessages(sessionId, page, size));
    }

    @PostMapping("/send/{sessionId}")
    public Result<ChatMessage> sendMessage(@RequestAttribute Long userId,
                                            @PathVariable Long sessionId,
                                            @RequestParam String content,
                                            @RequestParam(required = false) Integer msgType) {
        return Result.success(chatService.sendMessage(sessionId, userId, content, msgType));
    }

    @PostMapping("/recall/{messageId}")
    public Result<Void> recallMessage(@RequestAttribute Long userId,
                                       @PathVariable Long messageId) {
        chatService.recallMessage(messageId, userId);
        return Result.success();
    }
}
