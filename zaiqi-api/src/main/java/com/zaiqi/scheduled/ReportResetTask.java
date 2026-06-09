package com.zaiqi.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ReportResetTask {

    private final UserMapper userMapper;

    @Scheduled(cron = "0 0 * * * ?")
    public void resetReportCounts() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getLevel, 1)
                        .eq(User::getStatus, 1)
                        .isNotNull(User::getReportResetTime)
                        .lt(User::getReportResetTime, now));

        for (User user : users) {
            user.setFreeReportCount(5);
            user.setReportResetTime(now.plusMonths(1));
            userMapper.updateById(user);
        }

        if (!users.isEmpty()) {
            log.info("月度报告次数重置完成，共重置 {} 个用户", users.size());
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void checkVipExpiry() {
        LocalDateTime now = LocalDateTime.now();
        List<User> expiredUsers = userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getLevel, 1)
                        .eq(User::getStatus, 1)
                        .isNotNull(User::getVipExpireTime)
                        .lt(User::getVipExpireTime, now));

        for (User user : expiredUsers) {
            user.setLevel(0);
            user.setFreeReportCount(0);
            userMapper.updateById(user);
        }

        if (!expiredUsers.isEmpty()) {
            log.info("会员到期检查完成，共 {} 个用户降级", expiredUsers.size());
        }
    }
}
