package com.zaiqi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.admin.dto.AdminLoginRequest;
import com.zaiqi.admin.dto.AdminUserCreateRequest;
import com.zaiqi.admin.entity.AdminUser;
import com.zaiqi.admin.mapper.AdminUserMapper;
import com.zaiqi.admin.service.AdminUserService;
import com.zaiqi.auth.dto.LoginResponse;
import com.zaiqi.auth.jwt.JwtTokenProvider;
import com.zaiqi.common.BusinessException;
import com.zaiqi.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(AdminLoginRequest request) {
        AdminUser admin = adminUserMapper.selectOne(
                new LambdaQueryWrapper<AdminUser>()
                        .eq(AdminUser::getPhone, request.getPhone())
                        .eq(AdminUser::getStatus, 1));

        if (admin == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(admin.getId(), "ADMIN");
        String refreshToken = jwtTokenProvider.generateRefreshToken(admin.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(admin.getId())
                .nickname(admin.getUsername())
                .build();
    }

    @Override
    public void createAdmin(Long creatorId, AdminUserCreateRequest request) {
        AdminUser creator = adminUserMapper.selectById(creatorId);
        if (creator == null || creator.getRole() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        AdminUser admin = new AdminUser();
        admin.setUsername(request.getUsername());
        admin.setPhone(request.getPhone());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(request.getRole());
        admin.setStatus(1);
        adminUserMapper.insert(admin);
    }

    @Override
    public void toggleAdminStatus(Long operatorId, Long adminId) {
        AdminUser operator = adminUserMapper.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        AdminUser target = adminUserMapper.selectById(adminId);
        if (target == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        target.setStatus(target.getStatus() == 1 ? 0 : 1);
        adminUserMapper.updateById(target);
    }
}
