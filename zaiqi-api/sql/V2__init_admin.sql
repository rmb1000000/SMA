-- 初始化超级管理员（密码: admin123，BCrypt 加密）
INSERT INTO `zq_admin_user` (`id`, `username`, `phone`, `password`, `role`, `status`)
SELECT 1, '超级管理员', '13800000000',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       1, 1
WHERE NOT EXISTS (SELECT 1 FROM `zq_admin_user` WHERE `id` = 1);
