package com.zaiqi.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderResponse {
    private String orderNo;
    private Integer amount;
    private String productName;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String signType;
    private String paySign;
}
