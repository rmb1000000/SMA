package com.zaiqi.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaiqi.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {}
