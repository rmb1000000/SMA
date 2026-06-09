package com.zaiqi.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBasicInfoResponse {
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer birthYear;
    private String city;
    private Integer level;
    private Integer onlineStatus;
    private Boolean showOnline;
    private Boolean verified;
}
