package cn.com.yan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu")
public class Menu {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**菜单id*/
    @TableField(value = "menu")
    private String menu;

    /**菜单id*/
    @TableField(value = "role_id")
    private String role;
}
