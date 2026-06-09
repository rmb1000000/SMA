package com.zaiqi.feedback.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_feedback")
public class Feedback {
    private Long id;
    private Long userId;
    private String content;
    private String contact;
    private Integer status;
    private String reply;
    private Long handlerId;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;
}
