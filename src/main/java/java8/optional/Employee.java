package java8.optional;

import java.util.Objects;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/8 21:51
 * @version:1.0
 */
public class Employee {
    private String name;
    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Employee(String name, Double salary, int age, Status status) {
        this.name = name;
        this.status = status;
        this.salary = salary;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return age == employee.age &&
                Objects.equals(name, employee.name) &&
                Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary, age);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }

    public enum Status{
        FREE,
        BUSY,
        VOCATION;
    }

}
