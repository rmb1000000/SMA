package com.zaiqi.verification.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_user_verification")
public class UserVerification {
    private Long id;
    private Long userId;
    private Integer verifyType;
    private Integer verifyStatus;
    private String realName;
    private String idCard;
    private String idCardImageFront;
    private String idCardImageBack;
    private String holdIdCardImage;
    private LocalDateTime verifyTime;
    private String failReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
