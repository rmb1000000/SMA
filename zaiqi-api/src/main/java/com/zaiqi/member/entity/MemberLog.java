package com.zaiqi.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_member_log")
public class MemberLog {
    private Long id;
    private Long userId;
    private Integer oldLevel;
    private Integer newLevel;
    private LocalDateTime oldExpireTime;
    private LocalDateTime newExpireTime;
    private Integer changeType;
    private Long operatorId;
    private Long orderId;
    private String remark;
    private LocalDateTime createTime;
}
