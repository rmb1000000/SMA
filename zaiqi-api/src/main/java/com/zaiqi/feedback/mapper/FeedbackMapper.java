package com.zaiqi.feedback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaiqi.feedback.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {}
