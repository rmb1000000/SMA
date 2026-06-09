package com.zaiqi.user.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer birthYear;
    private String city;
    private String bio;
    private List<String> photos;
    private Integer maritalStatus;
    private Integer hasChildren;
    private String childrenCustody;
    private String occupation;
    private Integer education;
    private String annualIncome;

    private Integer dimValues;
    private Integer dimLifestyle;
    private Integer dimEconomy;
    private Integer dimFamily;
    private Integer dimEmotion;

    private List<String> riskFlags;
    private List<String> riskAutoTags;

    private Boolean phoneVerified;
    private Boolean identityVerified;
    private Boolean faceVerified;
}
