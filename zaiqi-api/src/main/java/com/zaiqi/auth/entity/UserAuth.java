package com.zaiqi.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_user_auth")
public class UserAuth extends BaseEntity {
    private Long userId;
    private String phone;
    private String password;
    private String wxOpenid;
    private String wxUnionid;
    private Integer phoneVerified;
}
