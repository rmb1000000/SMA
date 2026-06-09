-- 再启平台 V5: AI 决策引擎表

CREATE TABLE IF NOT EXISTS `zq_ai_report` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `target_user_id` BIGINT NOT NULL,
    `report_type` TINYINT DEFAULT 1 COMMENT '1-完整报告 2-单次分析',
    `report_data` JSON DEFAULT NULL,
    `summary` VARCHAR(500) DEFAULT NULL,
    `verdict` VARCHAR(50) DEFAULT NULL,
    `is_paid` TINYINT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI决策报告表';

CREATE TABLE IF NOT EXISTS `zq_ai_readiness` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `answers` JSON DEFAULT NULL,
    `report_data` JSON DEFAULT NULL,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='再婚Readiness评估表';
