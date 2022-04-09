package cn.com.yan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 *                         `course_name` varchar(255) DEFAULT NULL COMMENT '课程名称',
 *                         `course_code` varchar(255) DEFAULT NULL COMMENT '课程编码',
 *                         `course_time` varchar(50) DEFAULT NULL COMMENT '课时',
 *                         `theory_ability` int(30) DEFAULT NULL COMMENT '专业理论能力指标',
 *                         `code_ability` int(30) DEFAULT NULL COMMENT '专业编程能力指标',
 *                         `practice_ability` int(30) DEFAULT NULL COMMENT '实践能力指标',
 *                         `create_ability` int(30) DEFAULT NULL COMMENT '创新能力指标',
 *                         `art_ability` int(30) DEFAULT NULL COMMENT '艺术表现能力指标',
 */

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**课程名称*/
    @TableField(value = "course_name",exist=true)
    private String courseName;
    /**课程编码*/
    @TableField(value = "course_code",exist=true)
    private String courseCode;
    /**课时*/
    @TableField(value = "course_time",exist=true)
    private String courseTime;
    /**专业理论能力指标*/
    @TableField(value = "theory_ability",exist=true)
    private String theoryAbility;
    /**专业编程能力指标*/
    @TableField(value = "code_ability",exist=true)
    private String codeAbility;
    /**实践能力指标*/
    @TableField(value = "practice_ability",exist=true)
    private String practiceAbility;
    /**创新能力指标*/
    @TableField(value = "create_ability",exist=true)
    private String createAbility;
    /**艺术表现能力指标*/
    @TableField(value = "art_ability",exist=true)
    private String artAbility;
    @TableField(exist=false)
    private String status;
}
