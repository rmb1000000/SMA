-- 再启平台 V4: 社交系统表

CREATE TABLE IF NOT EXISTS `zq_chat_session` (
    `id` BIGINT NOT NULL,
    `user_a` BIGINT NOT NULL,
    `user_b` BIGINT NOT NULL,
    `last_message` TEXT DEFAULT NULL,
    `last_message_time` DATETIME DEFAULT NULL,
    `status` TINYINT DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_users` (`user_a`, `user_b`),
    INDEX `idx_last_message_time` (`last_message_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天会话表';

CREATE TABLE IF NOT EXISTS `zq_chat_message` (
    `id` BIGINT NOT NULL,
    `session_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `msg_type` TINYINT DEFAULT 1 COMMENT '1-文本 2-图片',
    `status` TINYINT DEFAULT 1 COMMENT '1-正常 2-已撤回',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_session_id` (`session_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

CREATE TABLE IF NOT EXISTS `zq_post` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `images` VARCHAR(2000) DEFAULT NULL,
    `visibility` TINYINT DEFAULT 1 COMMENT '1-全部可见 2-仅高级会员',
    `like_count` INT DEFAULT 0,
    `comment_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态表';

CREATE TABLE IF NOT EXISTS `zq_post_like` (
    `id` BIGINT NOT NULL,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_post_user` (`post_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态点赞表';

CREATE TABLE IF NOT EXISTS `zq_post_comment` (
    `id` BIGINT NOT NULL,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态评论表';
