package com.zaiqi.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_order")
public class Order extends BaseEntity {
    private String orderNo;
    private Long userId;
    private Integer productType;
    private String productName;
    private Integer amount;
    private String currency;
    private Integer status;
    private String wxTransactionId;
    private String wxNotifyRaw;
    private LocalDateTime paidTime;
    private LocalDateTime refundTime;
    private LocalDateTime closeTime;
    private String remark;
}
