package com.zaiqi.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WxLoginRequest {
    @NotBlank(message = "微信code不能为空")
    private String code;

    private String deviceInfo;
}
