package com.example.batchprocessing.javase.reflection;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.javase.reflection
 * @author:xiaoyige
 * @createTime:2021/11/4 21:49
 * @version:1.0
 */
public class User {
    private String name;
    private int id;
    private int age;

    public User(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
