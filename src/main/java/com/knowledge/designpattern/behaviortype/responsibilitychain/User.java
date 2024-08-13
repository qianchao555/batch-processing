package com.knowledge.designpattern.behaviortype.responsibilitychain;

/**
 * @ClassName User
 * @Author qianchao
 * @Date 2021/11/29
 * @Version designpattern V1.0
 **/
public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }
}
