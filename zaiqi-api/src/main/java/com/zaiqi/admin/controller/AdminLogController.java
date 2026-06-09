package com.zaiqi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.admin.entity.AdminLog;
import com.zaiqi.admin.mapper.AdminLogMapper;
import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
public class AdminLogController {

    private final AdminLogMapper adminLogMapper;

    @GetMapping
    public Result<PageResult<AdminLog>> listLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminLog> p = new Page<>(page, size);
        LambdaQueryWrapper<AdminLog> w = new LambdaQueryWrapper<AdminLog>().orderByDesc(AdminLog::getCreateTime);
        return Result.success(PageResult.of(adminLogMapper.selectPage(p, w)));
    }
}
