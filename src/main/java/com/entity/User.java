package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:23
 * @version:1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private  Long id;

    @TableField("name")
    private String name;

    @TableField("gender")
    private String gender;

    @TableField("email")
    private String email;

    @TableField("grils")
    private String grils;
}
