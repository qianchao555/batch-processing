package com.java8.lamda.methodreference;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 1. 对象：：实例方法名
 * 2. 类：：静态方法名
 * 3. 类：：实例方法名
 *
 * @author:xiaoyige
 * @createTime:2021/12/11 21:36
 * @version:1.0
 */
public class Demo {

    @Test
    public void test1() {
        Consumer<String> consumer = (x) -> System.out.println(x);
        consumer.accept("bbb");
        //方法引用
        //System.out.println(x) lambda表达式体已经有方法实现了
        Consumer<String> consumer2 = System.out::println;
        consumer2.accept("abc");

    }

    //对象：：实例方法名
    @Test
    public void test2() {
        Employee employee = new Employee("qc", 123.1, 12);
        Supplier<String> supplier = () -> {
            return employee.getName();
        };
        System.out.println(supplier.get());
        //方法引用
        //employee.getName() lambda表达式体已经有方法实现了
        //对象：：实例方法名
        Supplier<String> supplier2 = employee::getName;
        System.out.println(supplier2.get());
    }

    //类：：静态方法名
    @Test
    public void test3() {
        Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
        comparator.compare(5, 56);
        Comparator<Integer> comparator2 = Integer::compareTo;
        comparator.compare(89, 44);

    }

    //类：：实例方法名
    @Test
    public void test4() {
        BiPredicate<String, String> biPredicate = (str1, str2) -> str1.equals(str2);
        System.out.println(biPredicate.test("abc", "abc"));
        BiPredicate<String, String> biPredicate2 = String::equals;

    }

    //类：：实例方法名
    @Test
    public void test5() {
        Supplier<Employee> supplier = () -> new Employee("qc2", 234.2, 11);
        System.out.println(supplier.get());

        //构造器参数列表需要与函数式接口需要的参数对应
        //这里调用的构造器是带有一个参数的
        Function<String,Employee> supplier2 =Employee::new;
        Employee qcc = supplier2.apply("qcc");
        System.out.println(qcc);

    }

    //数组引用
    //Type::new
    @Test
    public void test6() {
        Function<Integer,String[]> function =(x)->new String[x];
        String[] strings = function.apply(2);
        System.out.println(strings.length);

        Function<Integer,String[]> function2 =String[]::new;
        function2.apply(5);



    }
}
