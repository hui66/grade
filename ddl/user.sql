
-- 学校，姓名，学号，年级，专业，性别，民族，电话
CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `user_name` varchar(30) DEFAULT NULL COMMENT '姓名',
                        `password` int(11) DEFAULT NULL COMMENT '密码',
                        `grade` varchar(50) DEFAULT NULL COMMENT '年级',
                        `age` varchar(30) DEFAULT NULL COMMENT '年龄',
                        `school` varchar(30) DEFAULT NULL COMMENT '学校',
                        `role` varchar(30) DEFAULT NULL COMMENT '角色',
                        `profession` varchar(30) DEFAULT NULL COMMENT '专业',
                        `sex` varchar(30) DEFAULT NULL COMMENT '性别',
                        `ethnic_group` varchar(30) DEFAULT NULL COMMENT '民族',
                        `phone` varchar(30) DEFAULT NULL COMMENT '电话',
                        `school_number` varchar(30) DEFAULT NULL COMMENT '学号',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT '用户表';