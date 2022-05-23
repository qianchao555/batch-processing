package com.easypoiexceldemo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/2/10 20:54
 * @version:1.0
 */
public class User {
    @Excel(name = "id")
    @NotBlank(message = "该字段不能为空")
    private String id;

    @Excel(name = "姓名")
    @Pattern(regexp = "[\\u4E00-\\u9FA5]{2,5}", message = "姓名中文2-5位")
    private String name;

    @Max(value = 20)
    @Excel(name = "年龄")
    private Integer age;

    @Excel(name = "生日", importFormat = "yyyy-MM-dd")
    private Date birthday;

    public User() {
    }

    public User(String id, String name, Integer age, Date birthday) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
