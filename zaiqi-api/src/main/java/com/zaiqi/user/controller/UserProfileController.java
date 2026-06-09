package com.zaiqi.user.controller;

import com.zaiqi.common.Result;
import com.zaiqi.user.dto.UserProfileRequest;
import com.zaiqi.user.dto.UserProfileResponse;
import com.zaiqi.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{targetUserId}")
    public Result<UserProfileResponse> getProfile(@RequestAttribute Long userId,
                                                   @PathVariable Long targetUserId) {
        return Result.success(userProfileService.getProfile(targetUserId, userId));
    }

    @PostMapping
    public Result<Void> saveProfile(@RequestAttribute Long userId,
                                     @Valid @RequestBody UserProfileRequest request) {
        userProfileService.saveOrUpdateProfile(userId, request);
        return Result.success();
    }
}
