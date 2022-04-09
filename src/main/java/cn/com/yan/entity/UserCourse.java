package cn.com.yan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 *                         `user_id` int(11) DEFAULT NULL COMMENT '用户id',
 *                         `course_id` int(11) DEFAULT NULL COMMENT '课程id',
 *                         `normal_grade` varchar(50) DEFAULT NULL COMMENT '平时分分数',
 *                         `end_grade` varchar(50) DEFAULT NULL COMMENT '期末考试分分数',
 *                         `status` varchar(50) DEFAULT NULL COMMENT '状态 1.代表选择之后还能取消 2代表成绩已出无法取消',
 * */
@Data
@TableName("userCourse")
public class UserCourse {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**用户id*/
    @TableField(value = "user_id")
    private String userId;
    /**课程id*/
    @TableField(value = "course_id")
    private String courseId;
    /**平时分分数*/
    @TableField(value = "normal_grade")
    private String normalGrade;
    /**期末考试分分数*/
    @TableField(value = "end_grade")
    private String endGrade;
    /**状态 1.代表选择之后还能取消 2代表成绩已出无法取消*/
    @TableField(value = "status")
    private String status;
    /**课程名称*/
    @TableField(exist=false)
    private String courseName;
    /**课程编码*/
    @TableField(exist=false)
    private String courseCode;
    /**专业理论能力指标*/
    @TableField(exist=false)
    private String theoryAbility;
    /**专业编程能力指标*/
    @TableField(exist=false)
    private String codeAbility;
    /**实践能力指标*/
    @TableField(exist=false)
    private String practiceAbility;
    /**创新能力指标*/
    @TableField(exist=false)
    private String createAbility;
    /**艺术表现能力指标*/
    @TableField(exist=false)
    private String artAbility;
    /**专业理论能力指标*/
    @TableField(exist=false)
    private String realTheoryAbility;
    /**专业编程能力指标*/
    @TableField(exist=false)
    private String realCodeAbility;
    /**实践能力指标*/
    @TableField(exist=false)
    private String realPracticeAbility;
    /**创新能力指标*/
    @TableField(exist=false)
    private String realCreateAbility;
    /**艺术表现能力指标*/
    @TableField(exist=false)
    private String realArtAbility;
    /**总分*/
    @TableField(exist=false)
    private String totalScore;
    /**各指标能力折算比例*/
    @TableField(exist=false)
    private String courseRate;

}
