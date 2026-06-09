package com.zaiqi.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_chat_message")
public class ChatMessage {
    private Long id;
    private Long sessionId;
    private Long senderId;
    private String content;
    private Integer msgType;
    private Integer status;
    private LocalDateTime createTime;
}
