package com.java8.optionaldemo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/5
 * @Version java8 V1.0
 **/
public class Demo {

    @Test
    public void test1() {
        //创建Optional对象
        //of(null)会抛出空指针异常
        Optional<Employee> op = Optional.of(new Employee("qc", 123.1, 12));
        Employee employee = op.get();
        Employee qqq = op.orElse(new Employee("qqq", 12312.3, 45));
        System.out.println(qqq);
        Employee yyy = op.orElseGet(() -> new Employee("yyy", 11.11, 8));
        Optional<String> op2 = Optional.ofNullable(null);
        String s = op2.orElseGet(() -> "woshi");
        System.out.println(s);
    }

    /**
     * map、flatMap
     * map(Function f):如果有值则对其处理，并返回处理后的Optional.否则返回Optional.empty()
     */
    @Test
    public void test2() {

        Optional<Employee> op = Optional.of(new Employee("qc", 111.1, 12));
        Optional<Employee> op2 = Optional.ofNullable(new Employee("qc", 111.1, 12));
        Optional<String> s = op2.map(e -> e.getName());
        Optional<Object> o = op2.flatMap(e -> Optional.ofNullable(e.getName()));
        System.out.println(o.get());

    }

    List<Employee> employeeList = Arrays.asList(
            new Employee("qc", 111.6, 34, Employee.Status.BUSY),
            new Employee("qq", 111.6, 34, Employee.Status.FREE),
            new Employee("qc2", 333.6, 34, Employee.Status.FREE),
            new Employee("qc3", 444.6, 9, Employee.Status.VOCATION),
            new Employee("qc4", 555.6, 89, Employee.Status.VOCATION),
            new Employee("qc5", 666.6, 8, Employee.Status.BUSY));

    @Test
    public void test3() {
        Optional<List<Employee>> employeeList = Optional.of(this.employeeList);
        Optional<List<String>> strings = employeeList.map(list -> list.stream().map(Employee::getName).collect(Collectors.toList()));
        System.out.println(strings.get());

    }

    @Test
    public void test4(){
        Car car=new Car();

        //方式一
        Optional<Car> optionalCar1=Optional.empty();

        //方式二
        Optional<Car> optionalCar2 = Optional.of(car);

        //方式三:推荐
        Optional<Car> optionalCar3 = Optional.ofNullable(car);
    }

}
