package com.zaiqi.post.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_post")
public class Post extends BaseEntity {
    private Long userId;
    private String content;
    private String images;
    private Integer visibility;
    private Integer likeCount;
    private Integer commentCount;
    private Integer status;
}
