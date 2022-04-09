CREATE TABLE `menu` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `role_id` varchar(30) DEFAULT NULL COMMENT '角色Id',
                        `menu` int(11) DEFAULT NULL COMMENT '菜单',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT '用户表';