package com.zaiqi.verification.service;

import com.zaiqi.verification.dto.VerificationRequest;

public interface VerificationService {
    void verifyIdentity(Long userId, VerificationRequest request);
    void submitFaceVerification(Long userId, String holdIdCardImage);
    boolean isIdentityVerified(Long userId);
}
