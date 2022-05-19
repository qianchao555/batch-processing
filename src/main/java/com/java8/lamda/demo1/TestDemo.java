package com.java8.lamda.demo1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/8 21:53
 * @version:1.0
 */
public class TestDemo {
    List<Employee> employeeList = Arrays.asList(
            new Employee("qc", 111.6, 3),
            new Employee("qc1", 222.6, 4),
            new Employee("qc2", 333.6, 34),
            new Employee("qc3", 444.6, 9),
            new Employee("qc4", 555.6, 89),
            new Employee("qc5", 666.6, 88));

    public List<Employee> filterEmp(List<Employee> list, MyPredicate<Employee> mp) {
        List<Employee> emps = new ArrayList<>();
        for (Employee employee : list) {
            if (mp.filter(employee)) {
                emps.add(employee);
            }
        }
        return emps;
    }

    /**
     * 对应Test5
     *
     * @param list
     * @param predicate
     * @return List<Employee>
     */
    public List<Employee> filterEmp2(List<Employee> list, Predicate<Employee> predicate) {
        //stream流会产生类似循环的效果去遍历List
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    @Test
    public void test5() {
        List<Employee> employees = filterEmp2(employeeList, employee -> employee.getAge()>80);
        employees.forEach(System.out::println);
    }

    /**
     * 对应Test6
     *
     * @param list
     * @param function
     * @return List<Employee>
     */
    public List<String> filterEmp3(List<Employee> list, Function<Employee, String> function) {
        //stream流会产生类似循环的效果去遍历List
        return list.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    @Test
    public void test6() {
        List<String> employees = filterEmp3(employeeList, employee -> employee.getName());
        employees.forEach(System.out::println);
    }


    //匿名内部类
    @Test
    public void test1() {
        List<Employee> employees = filterEmp(employeeList, new MyPredicate<Employee>() {
            @Override
            public boolean filter(Employee employee) {
                return employee.getAge() > 10;
            }
        });
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    //lambda
    @Test
    public void test2() {
        List<Employee> employees = filterEmp(employeeList, (x) -> {
            return x.getAge() > 40;
        });
        employees.forEach(System.out::println);
    }

    //Stream API
    @Test
    public void test3() {
        employeeList.stream()
                .filter(employee -> employee.getAge() > 80)
                .forEach(System.out::println);
    }
}
