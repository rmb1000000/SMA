-- 再启平台 V3: 支付与订单表

CREATE TABLE IF NOT EXISTS `zq_order` (
    `id` BIGINT NOT NULL,
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL,
    `product_type` TINYINT NOT NULL COMMENT '1-月度会员 2-季度会员 3-年度会员 4-AI分析 5-决策报告',
    `product_name` VARCHAR(100) NOT NULL,
    `amount` INT NOT NULL COMMENT '支付金额（分）',
    `currency` VARCHAR(3) DEFAULT 'CNY',
    `status` TINYINT DEFAULT 0 COMMENT '0-待支付 1-已支付 2-已退款 3-已关闭',
    `wx_transaction_id` VARCHAR(64) DEFAULT NULL,
    `wx_notify_raw` TEXT DEFAULT NULL,
    `paid_time` DATETIME DEFAULT NULL,
    `refund_time` DATETIME DEFAULT NULL,
    `close_time` DATETIME DEFAULT NULL,
    `remark` VARCHAR(500) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付订单表';

CREATE TABLE IF NOT EXISTS `zq_member_log` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `old_level` TINYINT DEFAULT 0,
    `new_level` TINYINT NOT NULL,
    `old_expire_time` DATETIME DEFAULT NULL,
    `new_expire_time` DATETIME DEFAULT NULL,
    `change_type` TINYINT NOT NULL COMMENT '1-开通 2-续费 3-禁用 4-到期 5-后台调整',
    `operator_id` BIGINT DEFAULT NULL,
    `order_id` BIGINT DEFAULT NULL,
    `remark` VARCHAR(200) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员变更日志表';
