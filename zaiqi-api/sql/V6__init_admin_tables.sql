-- 再启平台 V6: 管理后台辅助表

CREATE TABLE IF NOT EXISTS `zq_feedback` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `contact` VARCHAR(100) DEFAULT NULL,
    `status` TINYINT DEFAULT 0 COMMENT '0-待处理 1-已处理',
    `reply` VARCHAR(1000) DEFAULT NULL,
    `handler_id` BIGINT DEFAULT NULL,
    `handle_time` DATETIME DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表';

CREATE TABLE IF NOT EXISTS `zq_sensitive_word` (
    `id` BIGINT NOT NULL,
    `word` VARCHAR(100) NOT NULL,
    `level` TINYINT DEFAULT 1 COMMENT '1-提示 2-拦截',
    `scope` VARCHAR(100) DEFAULT 'all',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词表';

CREATE TABLE IF NOT EXISTS `zq_admin_log` (
    `id` BIGINT NOT NULL,
    `admin_id` BIGINT NOT NULL,
    `action` VARCHAR(200) NOT NULL,
    `target_type` VARCHAR(50) DEFAULT NULL,
    `target_id` BIGINT DEFAULT NULL,
    `detail` TEXT DEFAULT NULL,
    `ip` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员操作日志表';
