package com.zaiqi.auth.controller;

import com.zaiqi.admin.dto.AdminLoginRequest;
import com.zaiqi.admin.dto.AdminUserCreateRequest;
import com.zaiqi.admin.service.AdminUserService;
import com.zaiqi.auth.dto.LoginResponse;
import com.zaiqi.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        return Result.success(adminUserService.login(request));
    }

    @PostMapping("/admin-users")
    public Result<Void> createAdmin(@RequestAttribute Long userId,
                                     @Valid @RequestBody AdminUserCreateRequest request) {
        adminUserService.createAdmin(userId, request);
        return Result.success();
    }

    @PutMapping("/admin-users/{adminId}/toggle-status")
    public Result<Void> toggleAdminStatus(@RequestAttribute Long userId,
                                           @PathVariable Long adminId) {
        adminUserService.toggleAdminStatus(userId, adminId);
        return Result.success();
    }
}
