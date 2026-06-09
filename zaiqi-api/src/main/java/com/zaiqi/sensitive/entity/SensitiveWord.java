package com.zaiqi.sensitive.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_sensitive_word")
public class SensitiveWord extends BaseEntity {
    private String word;
    private Integer level;
    private String scope;
    private Integer status;
}
