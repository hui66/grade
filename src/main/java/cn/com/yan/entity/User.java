package cn.com.yan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 *                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 *                         `user_name` varchar(30) DEFAULT NULL COMMENT '姓名',
 *                         `password` int(11) DEFAULT NULL COMMENT '密码',
 *                         `grade` varchar(50) DEFAULT NULL COMMENT '年级',
 *                         `age` varchar(30) DEFAULT NULL COMMENT '年龄',
 *                         `school` varchar(30) DEFAULT NULL COMMENT '学校',
 *                         `role` varchar(30) DEFAULT NULL COMMENT '角色',
 *                         `profession` varchar(30) DEFAULT NULL COMMENT '专业',
 *                         `sex` varchar(30) DEFAULT NULL COMMENT '性别',
 *                         `ethnic_group` varchar(30) DEFAULT NULL COMMENT '民族',
 *                         `phone` varchar(30) DEFAULT NULL COMMENT '电话',
 *                         `school_number` varchar(30) DEFAULT NULL COMMENT '学号',
 *
 * */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    public Long id;
    /**名字*/
    @TableField(value = "user_name",exist=true)
    private String userName;
    /**密码*/
    @TableField(value = "password",exist=true)
    private String passWord;
    /**年龄*/
    @TableField(value = "age",exist=true)
    private String age;
    /**年级*/
    @TableField(value = "grade",exist=true)
    private String grade;
    /**角色*/
    @TableField(value = "role",exist=true)
    private String role;
    /**学校*/
    @TableField(value = "school",exist=true)
    private String school;
    /**专业*/
    @TableField(value = "profession",exist=true)
    private String profession;
    /**性别*/
    @TableField(value = "sex",exist=true)
    private String sex;
    /**民族*/
    @TableField(value = "ethnic_group",exist=true)
    private String ethnicGroup;
    /**电话*/
    @TableField(value = "phone",exist=true)
    private String phone;
    /**电话*/
    @TableField(value = "suggest",exist=true)
    private String suggest;
    /**school_number*/
    @TableField(value = "school_number",exist=true)
    private String schoolNumber;
    @TableField(exist=false)
    private List<String> menus;
}
