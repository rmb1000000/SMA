package com.zaiqi.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileRequest {
    @Size(max = 500, message = "个人简介不能超过500字")
    private String bio;

    private String photos;

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
}
