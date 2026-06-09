package com.zaiqi.member.service.impl;

import com.zaiqi.member.entity.MemberLog;
import com.zaiqi.member.mapper.MemberLogMapper;
import com.zaiqi.member.service.MemberService;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserMapper userMapper;
    private final MemberLogMapper memberLogMapper;

    @Override
    @Transactional
    public void handleProductPurchase(Long userId, Integer productType, Long orderId) {
        User user = userMapper.selectById(userId);
        if (user == null) return;

        int oldLevel = user.getLevel();
        LocalDateTime oldExpire = user.getVipExpireTime();
        LocalDateTime newExpire;
        int newLevel;
        String remark = "";
        int changeType;

        switch (productType) {
            case 1:
                newLevel = 1;
                newExpire = calculateExpireTime(oldExpire, 30);
                changeType = 1;
                remark = "开通月度会员";
                break;
            case 2:
                newLevel = 1;
                newExpire = calculateExpireTime(oldExpire, 90);
                changeType = 1;
                remark = "开通季度会员";
                break;
            case 3:
                newLevel = 1;
                newExpire = calculateExpireTime(oldExpire, 365);
                changeType = 1;
                remark = "开通年度会员";
                break;
            case 4:
            case 5:
                return;
            default:
                return;
        }

        user.setLevel(newLevel);
        user.setVipExpireTime(newExpire);
        userMapper.updateById(user);

        MemberLog logEntry = new MemberLog();
        logEntry.setUserId(userId);
        logEntry.setOldLevel(oldLevel);
        logEntry.setNewLevel(newLevel);
        logEntry.setOldExpireTime(oldExpire);
        logEntry.setNewExpireTime(newExpire);
        logEntry.setChangeType(changeType);
        logEntry.setOrderId(orderId);
        logEntry.setRemark(remark);
        logEntry.setCreateTime(LocalDateTime.now());
        memberLogMapper.insert(logEntry);

        if (oldLevel == 0) {
            user.setFreeReportCount(5);
            user.setReportResetTime(LocalDateTime.now().plusMonths(1));
            userMapper.updateById(user);
        }
    }

    @Override
    public Map<String, Object> getMemberStatus(Long userId) {
        User user = userMapper.selectById(userId);
        Map<String, Object> status = new HashMap<>();
        if (user == null) return status;

        boolean vipValid = user.getLevel() == 1
                && user.getVipExpireTime() != null
                && user.getVipExpireTime().isAfter(LocalDateTime.now());

        status.put("level", user.getLevel());
        status.put("levelName", user.getLevel() == 1 ? "高级会员" : "基础会员");
        status.put("vipExpireTime", user.getVipExpireTime());
        status.put("isVipValid", vipValid);
        status.put("freeReportCount", user.getFreeReportCount() != null ? user.getFreeReportCount() : 0);
        status.put("reportResetTime", user.getReportResetTime());
        return status;
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() == 0) return false;

        return switch (permission) {
            case "chat", "post", "like", "comment", "report_full" ->
                user.getLevel() == 1 && user.getVipExpireTime() != null
                        && user.getVipExpireTime().isAfter(LocalDateTime.now());
            case "ai_single" -> true;
            case "profile_view" -> true;
            default -> false;
        };
    }

    @Override
    @Transactional
    public boolean decrementReportCount(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getFreeReportCount() == null || user.getFreeReportCount() <= 0) {
            return false;
        }
        user.setFreeReportCount(user.getFreeReportCount() - 1);
        userMapper.updateById(user);
        return true;
    }

    @Override
    @Transactional
    public void disableMember(Long userId, Long operatorId) {
        User user = userMapper.selectById(userId);
        if (user == null) return;

        user.setStatus(0);
        userMapper.updateById(user);

        MemberLog logEntry = new MemberLog();
        logEntry.setUserId(userId);
        logEntry.setOldLevel(user.getLevel());
        logEntry.setNewLevel(user.getLevel());
        logEntry.setChangeType(3);
        logEntry.setOperatorId(operatorId);
        logEntry.setRemark("后台禁用");
        logEntry.setCreateTime(LocalDateTime.now());
        memberLogMapper.insert(logEntry);
    }

    @Override
    @Transactional
    public void enableMember(Long userId, Long operatorId) {
        User user = userMapper.selectById(userId);
        if (user == null) return;

        user.setStatus(1);
        userMapper.updateById(user);

        MemberLog logEntry = new MemberLog();
        logEntry.setUserId(userId);
        logEntry.setOldLevel(user.getLevel());
        logEntry.setNewLevel(user.getLevel());
        logEntry.setChangeType(5);
        logEntry.setOperatorId(operatorId);
        logEntry.setRemark("后台启用");
        logEntry.setCreateTime(LocalDateTime.now());
        memberLogMapper.insert(logEntry);
    }

    private LocalDateTime calculateExpireTime(LocalDateTime currentExpire, int days) {
        LocalDateTime base = currentExpire;
        if (base == null || base.isBefore(LocalDateTime.now())) {
            base = LocalDateTime.now();
        }
        return base.plusDays(days);
    }
}
