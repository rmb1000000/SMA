package com.zaiqi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_admin_user")
public class AdminUser extends BaseEntity {
    private String username;
    private String phone;
    private String password;
    private Integer role;
    private Integer status;
}
