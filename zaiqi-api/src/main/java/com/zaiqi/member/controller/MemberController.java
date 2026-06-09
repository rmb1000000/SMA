package com.zaiqi.member.controller;

import com.zaiqi.common.Result;
import com.zaiqi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/status")
    public Result<Map<String, Object>> getMemberStatus(@RequestAttribute Long userId) {
        return Result.success(memberService.getMemberStatus(userId));
    }

    @GetMapping("/prices")
    public Result<Map<String, Object>> getPriceList() {
        return Result.success(Map.of(
            "monthly", 6900,
            "quarterly", 18900,
            "yearly", 28900,
            "aiSingle", 990,
            "reportExtra", 990
        ));
    }
}
