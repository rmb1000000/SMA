package com.zaiqi.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_user")
public class User extends BaseEntity {
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer birthYear;
    private String city;
    private Integer status;
    private Integer level;
    private LocalDateTime vipExpireTime;
    private Integer freeReportCount;
    private LocalDateTime reportResetTime;
    private Integer showOnline;
}
