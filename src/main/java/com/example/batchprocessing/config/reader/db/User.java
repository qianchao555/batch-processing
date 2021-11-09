package com.example.batchprocessing.config.reader.db;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.config.reader.db
 * @author:xiaoyige
 * @createTime:2021/11/3 21:10
 * @version:1.0
 */
public class User {
    private Integer id;
    private String name;
    private String nation;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nation='" + nation + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }

    private Integer age;
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
