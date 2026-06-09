package com.zaiqi.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_chat_session")
public class ChatSession extends BaseEntity {
    private Long userA;
    private Long userB;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer status;
}
