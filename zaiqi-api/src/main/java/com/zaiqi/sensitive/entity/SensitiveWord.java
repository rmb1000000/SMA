package com.zaiqi.sensitive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_sensitive_word")
public class SensitiveWord {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String word;
    private Integer level;
    private String scope;
    private Integer status;
    private LocalDateTime createTime;
}
