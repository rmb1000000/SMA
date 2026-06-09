package com.zaiqi.review.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("zq_profile_review_record")
public class ProfileReviewRecord {
    private Long id;
    private Long userId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private Integer reviewStatus;
    private Long reviewerId;
    private String reviewComment;
    private LocalDateTime reviewTime;
    private LocalDateTime createTime;
}
