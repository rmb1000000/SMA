package com.zaiqi.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull(message = "商品类型不能为空")
    private Integer productType;
}
