CREATE TABLE `userCourse` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `user_id` int(11) DEFAULT NULL COMMENT '用户id',
                        `course_id` int(11) DEFAULT NULL COMMENT '课程id',
                        `normal_grade` varchar(50) DEFAULT NULL COMMENT '平时分分数',
                        `end_grade` varchar(50) DEFAULT NULL COMMENT '期末考试分分数',
                        `status` varchar(50) DEFAULT '0' COMMENT '状态 0.代表选择之后还能取消 1.代表成绩不合格则可以再次选择 2代表成绩合格且已出无法取消',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT '用户课程表';