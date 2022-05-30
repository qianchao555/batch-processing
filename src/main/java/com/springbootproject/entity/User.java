package com.springbootproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:23
 * @version:1.0
 */
@Data
@TableName("test_user")
public class User {
    private  String id;
    private String name;
    private String addr;
    private Integer age;
}
