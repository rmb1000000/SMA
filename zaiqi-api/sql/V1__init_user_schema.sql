-- 再启平台 V1: 用户系统基础表

-- 1. 用户基础表
CREATE TABLE IF NOT EXISTS `zq_user` (
    `id` BIGINT NOT NULL COMMENT '雪花ID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL（JSON数组，最多3张）',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未设置 1-男 2-女',
    `birth_year` INT DEFAULT NULL COMMENT '出生年份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '所在城市',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
    `level` TINYINT DEFAULT 0 COMMENT '会员等级：0-基础 1-高级',
    `vip_expire_time` DATETIME DEFAULT NULL COMMENT '高级会员到期时间',
    `free_report_count` INT DEFAULT 5 COMMENT '本月剩余免费决策报告次数',
    `report_reset_time` DATETIME DEFAULT NULL COMMENT '报告次数重置时间',
    `show_online` TINYINT DEFAULT 1 COMMENT '是否显示在线状态：1-显示 0-隐藏',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_level` (`level`),
    INDEX `idx_gender` (`gender`),
    INDEX `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础表';

-- 2. 用户认证信息表
CREATE TABLE IF NOT EXISTS `zq_user_auth` (
    `id` BIGINT NOT NULL COMMENT '雪花ID',
    `user_id` BIGINT NOT NULL,
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `password` VARCHAR(200) DEFAULT NULL COMMENT '密码（bcrypt）',
    `wx_openid` VARCHAR(100) DEFAULT NULL COMMENT '微信openId',
    `wx_unionid` VARCHAR(100) DEFAULT NULL COMMENT '微信unionId',
    `phone_verified` TINYINT DEFAULT 1 COMMENT '手机号已验证',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_wx_openid` (`wx_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户认证信息表';

-- 3. 用户详细资料表
CREATE TABLE IF NOT EXISTS `zq_user_profile` (
    `id` BIGINT NOT NULL COMMENT '雪花ID',
    `user_id` BIGINT NOT NULL,
    `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `photos` VARCHAR(2000) DEFAULT NULL COMMENT '生活照URL（JSON数组，最多6张）',
    `marital_status` TINYINT DEFAULT 0 COMMENT '婚姻状况：0-未设置 1-离异 2-丧偶 3-未婚',
    `has_children` TINYINT DEFAULT 0 COMMENT '有无子女：0-无 1-有',
    `children_custody` VARCHAR(200) DEFAULT NULL COMMENT '子女抚养情况说明',
    `occupation` VARCHAR(50) DEFAULT NULL COMMENT '职业',
    `education` TINYINT DEFAULT 0 COMMENT '学历',
    `annual_income` VARCHAR(50) DEFAULT NULL COMMENT '年收入范围',
    `dim_values` INT DEFAULT 0 COMMENT '价值观维度（0-20）',
    `dim_lifestyle` INT DEFAULT 0 COMMENT '生活方式维度（0-20）',
    `dim_economy` INT DEFAULT 0 COMMENT '经济观念维度（0-20）',
    `dim_family` INT DEFAULT 0 COMMENT '家庭观念维度（0-20）',
    `dim_emotion` INT DEFAULT 0 COMMENT '感性态度维度（0-20）',
    `profile_status` TINYINT DEFAULT 0 COMMENT '资料状态',
    `review_status` TINYINT DEFAULT 0 COMMENT '审核状态',
    `risk_flags` VARCHAR(500) DEFAULT NULL COMMENT '风险标记（JSON数组）',
    `risk_auto_tags` VARCHAR(500) DEFAULT NULL COMMENT '系统自动标记风险（JSON数组）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_review_status` (`review_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户详细资料表';

-- 4. 用户认证记录表
CREATE TABLE IF NOT EXISTS `zq_user_verification` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `verify_type` TINYINT NOT NULL COMMENT '1-手机号 2-身份实名 3-真人认证',
    `verify_status` TINYINT DEFAULT 0 COMMENT '0-未认证 1-已认证 2-失败',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(30) DEFAULT NULL COMMENT '身份证号（加密）',
    `id_card_image_front` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面',
    `id_card_image_back` VARCHAR(500) DEFAULT NULL COMMENT '身份证反面',
    `hold_id_card_image` VARCHAR(500) DEFAULT NULL COMMENT '手持身份证照片',
    `verify_time` DATETIME DEFAULT NULL,
    `fail_reason` VARCHAR(200) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_type` (`user_id`, `verify_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户认证记录表';

-- 5. 登录记录表
CREATE TABLE IF NOT EXISTS `zq_login_record` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `login_type` TINYINT NOT NULL COMMENT '1-微信 2-短信 3-密码',
    `ip` VARCHAR(50) DEFAULT NULL,
    `device_info` VARCHAR(200) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录记录表';

-- 6. 资料审核记录表
CREATE TABLE IF NOT EXISTS `zq_profile_review_record` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `field_name` VARCHAR(50) NOT NULL,
    `old_value` VARCHAR(500) DEFAULT NULL,
    `new_value` VARCHAR(500) DEFAULT NULL,
    `review_status` TINYINT DEFAULT 0,
    `reviewer_id` BIGINT DEFAULT NULL,
    `review_comment` VARCHAR(500) DEFAULT NULL,
    `review_time` DATETIME DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_review_status` (`review_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资料审核记录表';

-- 7. 管理员表
CREATE TABLE IF NOT EXISTS `zq_admin_user` (
    `id` BIGINT NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `role` TINYINT DEFAULT 1 COMMENT '1-超级管理员 2-普通管理员',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';
