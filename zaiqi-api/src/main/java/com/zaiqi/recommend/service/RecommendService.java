package com.zaiqi.recommend.service;

import com.zaiqi.recommend.dto.RecommendUserDTO;
import java.util.List;

public interface RecommendService {
    List<RecommendUserDTO> getRecommendations(Long userId, int count);
    void markNotInterested(Long userId, Long targetUserId);
    void recordProfileView(Long viewerId, Long targetUserId);
}
