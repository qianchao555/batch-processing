package com.collection.set.hashset;

import java.util.Objects;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/19 21:54
 * @version:1.0
 */
public class EmployeeTwo {
    private String name;
    private String sal;
    private MyDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeTwo that = (EmployeeTwo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(birthday, that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthday);
    }

    public EmployeeTwo(String name, String sal, MyDate birthday) {
        this.name = name;
        this.sal = sal;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "EmployeeTwo{" +
                "name='" + name + '\'' +
                ", sal='" + sal + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
class MyDate{
    private String year;
    private String month;
    private String day;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return Objects.equals(year, myDate.year) &&
                Objects.equals(month, myDate.month) &&
                Objects.equals(day, myDate.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    public MyDate(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
