package com.knowledge.java8.lamda.demo1;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/8 21:51
 * @version:1.0
 */
public class Employee {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    private Double salary;
    private int age;

    public Employee(String name, Double salary, int age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}
