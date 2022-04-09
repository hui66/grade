CREATE TABLE `course` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `course_name` varchar(255) DEFAULT NULL COMMENT '课程名称',
                        `course_code` varchar(255) DEFAULT NULL COMMENT '课程编码',
                        `course_time` varchar(50) DEFAULT NULL COMMENT '课时',
                        `theory_ability` int(30) DEFAULT NULL COMMENT '专业理论能力指标',
                        `code_ability` int(30) DEFAULT NULL COMMENT '专业编程能力指标',
                        `practice_ability` int(30) DEFAULT NULL COMMENT '实践能力指标',
                        `create_ability` int(30) DEFAULT NULL COMMENT '创新能力指标',
                        `art_ability` int(30) DEFAULT NULL COMMENT '艺术表现能力指标',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT '课程表';