package com.springbootproject.entity;

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
@TableName("test_user")
public class User {
    private  String id;
    private String name;
    private String addr;
    private Integer age;
}
