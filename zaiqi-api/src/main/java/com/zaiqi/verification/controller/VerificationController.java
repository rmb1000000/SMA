package com.zaiqi.verification.controller;

import com.zaiqi.common.Result;
import com.zaiqi.verification.dto.VerificationRequest;
import com.zaiqi.verification.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/identity")
    public Result<Void> verifyIdentity(@RequestAttribute Long userId,
                                        @Valid @RequestBody VerificationRequest request) {
        verificationService.verifyIdentity(userId, request);
        return Result.success();
    }

    @PostMapping("/face")
    public Result<Void> submitFaceVerification(@RequestAttribute Long userId,
                                                @RequestParam String holdIdCardImage) {
        verificationService.submitFaceVerification(userId, holdIdCardImage);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Map<String, Boolean>> getVerificationStatus(@RequestAttribute Long userId) {
        boolean verified = verificationService.isIdentityVerified(userId);
        return Result.success(Map.of("identityVerified", verified));
    }
}
