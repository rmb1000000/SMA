package com.zaiqi.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_user_profile")
public class UserProfile extends BaseEntity {
    private Long userId;
    private String bio;
    private String photos;
    private Integer maritalStatus;
    private Integer hasChildren;
    private String childrenCustody;
    private String occupation;
    private Integer education;
    private String annualIncome;
    private Integer dimValues;
    private Integer dimLifestyle;
    private Integer dimEconomy;
    private Integer dimFamily;
    private Integer dimEmotion;
    private Integer profileStatus;
    private Integer reviewStatus;
    private String riskFlags;
    private String riskAutoTags;
}
