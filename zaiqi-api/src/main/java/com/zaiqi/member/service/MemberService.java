package com.zaiqi.member.service;

import java.util.Map;

public interface MemberService {
    void handleProductPurchase(Long userId, Integer productType, Long orderId);
    Map<String, Object> getMemberStatus(Long userId);
    boolean hasPermission(Long userId, String permission);
    boolean decrementReportCount(Long userId);
    void disableMember(Long userId, Long operatorId);
    void enableMember(Long userId, Long operatorId);
}
