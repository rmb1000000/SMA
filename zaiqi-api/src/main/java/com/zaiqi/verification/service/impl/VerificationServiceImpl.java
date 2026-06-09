package com.zaiqi.verification.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import com.zaiqi.verification.dto.VerificationRequest;
import com.zaiqi.verification.entity.UserVerification;
import com.zaiqi.verification.mapper.UserVerificationMapper;
import com.zaiqi.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserVerificationMapper verificationMapper;

    @Override
    @Transactional
    public void verifyIdentity(Long userId, VerificationRequest request) {
        if (!IdcardUtil.isValidCard(request.getIdCard())) {
            throw new BusinessException(ErrorCode.VERIFICATION_FAILED);
        }

        UserVerification existing = verificationMapper.selectOne(
                new LambdaQueryWrapper<UserVerification>()
                        .eq(UserVerification::getUserId, userId)
                        .eq(UserVerification::getVerifyType, 2));

        if (existing != null && existing.getVerifyStatus() == 1) {
            return;
        }

        UserVerification verification = existing != null ? existing : new UserVerification();
        verification.setUserId(userId);
        verification.setVerifyType(2);
        verification.setRealName(request.getRealName());
        verification.setIdCard(request.getIdCard());
        verification.setIdCardImageFront(request.getIdCardImageFront());
        verification.setIdCardImageBack(request.getIdCardImageBack());
        verification.setVerifyStatus(1);
        verification.setVerifyTime(LocalDateTime.now());

        if (existing != null) {
            verificationMapper.updateById(verification);
        } else {
            verificationMapper.insert(verification);
        }
    }

    @Override
    @Transactional
    public void submitFaceVerification(Long userId, String holdIdCardImage) {
        UserVerification verification = new UserVerification();
        verification.setUserId(userId);
        verification.setVerifyType(3);
        verification.setHoldIdCardImage(holdIdCardImage);
        verification.setVerifyStatus(0);
        verificationMapper.insert(verification);
    }

    @Override
    public boolean isIdentityVerified(Long userId) {
        UserVerification verification = verificationMapper.selectOne(
                new LambdaQueryWrapper<UserVerification>()
                        .eq(UserVerification::getUserId, userId)
                        .eq(UserVerification::getVerifyType, 2)
                        .eq(UserVerification::getVerifyStatus, 1));
        return verification != null;
    }
}
