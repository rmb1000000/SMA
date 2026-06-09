package com.zaiqi.admin.service;

import com.zaiqi.admin.dto.AdminLoginRequest;
import com.zaiqi.admin.dto.AdminUserCreateRequest;
import com.zaiqi.auth.dto.LoginResponse;

public interface AdminUserService {
    LoginResponse login(AdminLoginRequest request);
    void createAdmin(Long creatorId, AdminUserCreateRequest request);
    void toggleAdminStatus(Long operatorId, Long adminId);
}
