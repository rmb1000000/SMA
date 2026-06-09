package com.zaiqi.order.dto;

import lombok.Data;

@Data
public class WxPayNotifyRequest {
    private String id;
    private String createTime;
    private String eventType;
    private String summary;
    private Resource resource;

    @Data
    public static class Resource {
        private String algorithm;
        private String ciphertext;
        private String originalType;
        private String associatedData;
        private String nonce;
    }
}
