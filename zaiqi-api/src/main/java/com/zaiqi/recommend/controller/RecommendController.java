package com.zaiqi.recommend.controller;

import com.zaiqi.common.Result;
import com.zaiqi.recommend.dto.RecommendUserDTO;
import com.zaiqi.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public Result<List<RecommendUserDTO>> getRecommendations(
            @RequestAttribute Long userId,
            @RequestParam(defaultValue = "20") int count) {
        return Result.success(recommendService.getRecommendations(userId, count));
    }

    @PostMapping("/{targetUserId}/not-interested")
    public Result<Void> markNotInterested(@RequestAttribute Long userId,
                                           @PathVariable Long targetUserId) {
        recommendService.markNotInterested(userId, targetUserId);
        return Result.success();
    }

    @PostMapping("/{targetUserId}/view")
    public Result<Void> recordView(@RequestAttribute Long userId,
                                    @PathVariable Long targetUserId) {
        recommendService.recordProfileView(userId, targetUserId);
        return Result.success();
    }
}
