package com.zaiqi.user.controller;

import com.zaiqi.common.Result;
import com.zaiqi.user.dto.PrivacySettingsRequest;
import com.zaiqi.user.dto.UserBasicInfoResponse;
import com.zaiqi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public Result<UserBasicInfoResponse> getBasicInfo(@RequestAttribute Long userId) {
        return Result.success(userService.getBasicInfo(userId));
    }

    @PutMapping("/info")
    public Result<Void> updateBasicInfo(
            @RequestAttribute Long userId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) Integer birthYear,
            @RequestParam(required = false) String city) {
        userService.updateBasicInfo(userId, nickname, gender, birthYear, city);
        return Result.success();
    }

    @PutMapping("/avatar")
    public Result<Void> updateAvatar(@RequestAttribute Long userId, @RequestParam String avatar) {
        userService.updateAvatar(userId, avatar);
        return Result.success();
    }

    @GetMapping("/info/{targetUserId}")
    public Result<UserBasicInfoResponse> getUserInfo(@RequestAttribute Long userId,
                                                      @PathVariable Long targetUserId) {
        return Result.success(userService.getBasicInfo(targetUserId));
    }

    @PutMapping("/privacy")
    public Result<Void> updatePrivacy(@RequestAttribute Long userId,
                                       @Valid @RequestBody PrivacySettingsRequest request) {
        userService.updatePrivacy(userId, request);
        return Result.success();
    }

    @PostMapping("/heartbeat")
    public Result<Void> heartbeat(@RequestAttribute Long userId) {
        userService.setOnlineStatus(userId, true);
        return Result.success();
    }
}
