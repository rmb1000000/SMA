package com.zaiqi.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_login_record")
public class LoginRecord {
    private Long id;
    private Long userId;
    private Integer loginType;
    private String ip;
    private String deviceInfo;
    private LocalDateTime createTime;
}
