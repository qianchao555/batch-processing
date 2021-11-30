package lamda.predicate;

import lombok.Data;

@Data
public class Employee {
/**
 * @ClassName Employee
 * @Author qianchao
 * @Date 2021/11/2 
 * @Version OPRA V1.0
 **/
private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private Double salary;
private int age;
    public Employee(String name, Double salary, int age) {
        this.name = name;
        this.salary = salary;
        this.age = age;
    }
}
