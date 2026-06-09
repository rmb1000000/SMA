# S3 推荐与匹配系统 — 实施计划

**Goal:** 实现基于 5 维匹配分 + 行为信号的混合推荐算法，包含推荐池管理、每日刷新、权限控制。

**Architecture:** 纯后端算法 + SQL 计算。匹配分在用户资料更新时缓存到 Redis，推荐时通过 SQL 排序 + 行为加权得到最终推荐列表。

**Tech Stack:** Java 21, Spring Boot 3.2.5, MyBatis-Plus, MySQL 8.0, Redis 7.x

---

## 文件结构

```
zaiqi-api/src/main/java/com/zaiqi/
└── recommend/
    ├── service/
    │   ├── RecommendService.java
    │   └── impl/RecommendServiceImpl.java
    ├── controller/
    │   └── RecommendController.java
    └── dto/
        └── RecommendUserDTO.java
```

## 实施任务

### Task 1: 推荐系统骨架

- [ ] **Step 1: 创建 RecommendUserDTO.java**

```java
package com.zaiqi.recommend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RecommendUserDTO {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer birthYear;
    private String city;
    private String bio;
    private Integer matchScore;
    private List<String> riskFlags;
    private List<String> riskAutoTags;
    private Boolean identityVerified;
    private Integer photosCount;
}
```

- [ ] **Step 2: 创建 RecommendService + RecommendServiceImpl**

```java
// RecommendService.java
package com.zaiqi.recommend.service;

import com.zaiqi.recommend.dto.RecommendUserDTO;
import java.util.List;

public interface RecommendService {
    List<RecommendUserDTO> getRecommendations(Long userId, int count);
    void markNotInterested(Long userId, Long targetUserId);
    void recordProfileView(Long viewerId, Long targetUserId);
}
```

RecommendServiceImpl 中实现核心推荐算法：
- 查询异性用户（排除已推荐/已屏蔽/自己）
- 匹配分 = 5维差距计算
- 行为信号分 = 资料完整度 + 活跃度 + 照片数 + 互动热度 + 响应率 + 认证加分 - 负向信号
- 最终分 = 匹配分 × 60% + 行为信号分 × 40%
- 基础会员：每日限制次数（Redis 计数器）
- 高级会员：无限制

- [ ] **Step 3: 创建 RecommendController**

```java
// RecommendController.java
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
```

- [ ] **Step 4: 编译验证**

需创建目录：`zaiqi-api/src/main/java/com/zaiqi/recommend/service/impl/` 和 `recommend/dto/` 和 `recommend/controller/`

- [ ] **Step 5: Commit**

```bash
git add zaiqi-api/src/main/java/com/zaiqi/recommend/
git commit -m "feat: 实现推荐系统骨架和接口"
```

---

## RecommendServiceImpl 核心算法说明

```java
// 伪代码实现
public List<RecommendUserDTO> getRecommendations(Long userId, int count) {
    // 1. 当前用户信息
    User user = userMapper.selectById(userId);
    
    // 2. 检查推荐限制（基础会员每日限制）
    String dailyKey = "recommend:daily:" + userId + ":" + LocalDate.now();
    Integer used = redisTemplate.opsForValue().get(dailyKey);
    if (user.getLevel() == 0 && used != null && used >= 20) {
        throw new BusinessException("每日推荐次数已用完");
    }
    
    // 3. 查询候选用户（异性, 非禁用, 未删除）
    List<User> candidates = userMapper.selectList(...);
    
    // 4. 过滤已推荐/已屏蔽
    Set<String> excluded = redisTemplate.opsForSet().members("recommend:excluded:" + userId);
    
    // 5. 计算匹配分和行为分
    List<RecommendUserDTO> result = candidates.stream()
        .filter(c -> !excluded.contains(String.valueOf(c.getId())))
        .map(c -> {
            int matchScore = calculateMatchScore(user, c);
            int behaviorScore = calculateBehaviorScore(c);
            int finalScore = (int)(matchScore * 0.6 + behaviorScore * 0.4);
            return toDTO(c, finalScore);
        })
        .sorted((a, b) -> b.getMatchScore() - a.getMatchScore())
        .limit(count)
        .toList();
    
    // 6. 记录已推荐
    redisTemplate.opsForSet().add("recommend:excluded:" + userId, 
        result.stream().map(r -> String.valueOf(r.getUserId())).toArray());
    
    // 7. 更新计数
    if (user.getLevel() == 0) {
        redisTemplate.opsForValue().increment(dailyKey);
    }
    
    return result;
}

private int calculateMatchScore(User current, User target) {
    // 从 UserProfile 读取 5 维数据
    // 总分 = Σ(20 - |差异|) 
    // 返回 0-100
}

private int calculateBehaviorScore(User target) {
    // 资料完整度 + 活跃度 + 照片 + 互动 + 认证 - 负向
}
```
