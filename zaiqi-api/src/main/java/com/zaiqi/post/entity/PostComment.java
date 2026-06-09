package com.zaiqi.post.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zaiqi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zq_post_comment")
public class PostComment extends BaseEntity {
    private Long postId;
    private Long userId;
    private String content;
    private Integer status;
}
