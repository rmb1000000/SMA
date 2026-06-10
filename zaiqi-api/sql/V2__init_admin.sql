-- 初始化超级管理员（密码: admin123，BCrypt 加密）
INSERT INTO `zq_admin_user` (`id`, `username`, `phone`, `password`, `role`, `status`)
SELECT 1, '超级管理员', '13800000000',
       '$2b$10$sS/YndqmeCu.Qz97R.XVMeGTVR8XpUrOw5U1tDDXY.P9dBuBHQVv.',
       1, 1
WHERE NOT EXISTS (SELECT 1 FROM `zq_admin_user` WHERE `id` = 1);
