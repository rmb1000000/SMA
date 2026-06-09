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
