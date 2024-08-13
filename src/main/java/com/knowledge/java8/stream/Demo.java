package com.knowledge.java8.stream;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream的三个操作步骤
 *
 * @author:xiaoyige
 * @createTime:2021/12/12 18:39
 * @version:1.0
 */
public class Demo {
    List<Employee> employeeList = Arrays.asList(
            new Employee("qc", 111.6, 34, Employee.Status.BUSY),
            new Employee("qq", 111.6, 34, Employee.Status.FREE),
            new Employee("qc2", 333.6, 34, Employee.Status.FREE),
            new Employee("qc3", 444.6, 9, Employee.Status.VOCATION),
            new Employee("qc4", 555.6, 89, Employee.Status.VOCATION),
            new Employee("qc5", 666.6, 8, Employee.Status.BUSY));

    @Test
    public void test1() {
        //集合提供的stream()
//        List<String> list = new ArrayList<>();
//        Stream<String> stream1 = list.stream();

        //Arrays.stream()获取流
//        Employee[] emps = new Employee[10];
//        Stream<Employee> stream2 = Arrays.stream(emps);
//
//        //Stream.of()
        Stream<String> stream3 = Stream.of("abc", "bbc", "ccc");
        stream3.forEach(System.out::print);

        //抛异常：流只能遍历一次，遍历完后，这个流就被消费掉 关闭了
//        stream3.forEach(System.out::print);



        List<String> list = Arrays.asList("abc", "bbc", "ccc");

//        //无限流--迭代
        Stream.iterate(0, (x) -> x + 2)
                .limit(7)
                .map(x -> list.stream().collect(Collectors.toList()))
                .forEach(System.out::println);
        System.out.println("------------------------");

        //每一个元素映射成想要的

//        List<List<String>> collect = Stream.iterate(0, (x) -> x + 2)
//                .limit(7)
//                //每一个元素映射成想要的
//            .map(x -> list.stream().collect(Collectors.toList()))
//                .filter(a -> !a.isEmpty())
//                .collect(Collectors.toList());

//        //无限流--生成
//        Stream.generate(()->Math.random())
//                .limit(7)
//                .forEach(System.out::println);
    }

    /**
     * 中间操作-删选与切片
     */
    @Test
    public void test2() {
        //filter过滤
//        List<Employee> collect = employeeList.stream()
//                .filter((employee -> employee.getAge() > 35))
//                .collect(Collectors.toList());
//        System.out.println(collect);
        employeeList.stream()
                .filter((employee -> {
                    System.out.println("filter中间操作");
                    return employee.getAge() > 30;
                }))
                //截断流,找到满足条件的条数后，便不会去迭代查找了。一定程度上提高了效率
                .limit(2)
                //终止操作
                .forEach(System.out::println);

    }

    /**
     * 中间操作-删选与切片
     */
    @Test
    public void test3() {
        employeeList.stream()
                .filter((employee -> {
                    System.out.println("filter中间操作");
                    return employee.getAge() > 30;
                }))
                //跳过第几个
//                .skip(0)
                //通过流生成的hashCode和equals()去除重复元素的。所以对象的这两个方法需要重写
                // new Employee("qc", 111.6, 34),
                // new Employee("qc", 111.6, 34),
                //因为重写了方法，所以能达到去重的效果
                .distinct()
                //终止操作
                .forEach(System.out::println);

    }

    /**
     * 中间操作-映射  重要
     * map-接收lambda,将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，
     * 并将其映射成一个新的元素
     * flatMap-接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test4() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
        list.stream()
                .map((str) -> str.toUpperCase())
                .forEach(System.out::println);
        System.out.println("---------------------------");
        //提取员工的名字
        System.out.println("所有员工的名字");
        List<String> collect = employeeList.stream()
//                .map((employee -> employee.getName()))
                .map(Employee::getName)
                .collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("-----------双重流----------------");
        //流里面还是流
        Stream<Stream<Character>> streamStream = list.stream()
                .map(this::filterCharacter);
        //双重流的遍历
        streamStream.forEach((stream) -> {
            stream.forEach(System.out::println);
        });
    }

    //将字符串转换为字符流
    public Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();

    }

    /**
     * 中间操作-映射  重要
     * map-接收lambda,将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，
     * 并将其映射成一个新的元素
     * flatMap-接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test5() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        System.out.println("-----------map操作会导致双重流----------------");
        //流里面还是流
        Stream<Stream<Character>> streamStream = list.stream()
                .map(this::filterCharacter);
        //{{a,a,a},{b,b,b}}
        //双重流的遍历
        streamStream.forEach((stream) -> {
            stream.forEach(System.out::println);
        });
        System.out.println("-----------flatMap----------------");
        //{a,a,a,b,b,b}
        list.stream()
                .flatMap(this::filterCharacter)
                .forEach(System.out::println);

        //联想
        System.out.println("-----------联想list add(object o) addAll(Collection c)----------------");
        List<String> list1 = Arrays.asList("a", "bb");
        List list2 = new ArrayList<>();
        list2.add("c");
        list2.add("dd");
        //list里面包含list,list1作为一个集合，添加到list
//        list2.add(list1);
        System.out.println(list2);
        //只有一个list,List1里面的值，添加到了list
        list2.addAll(list1);
        System.out.println(list2);

    }

    /**
     * 中间操作-排序
     * sorted() 自然排序(Comparable)  也就是按照compareTo()方法去排序
     * sorted(Comparator c) 定制排序（comparator）
     */
    @Test
    public void test6() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
        list.stream()
                .sorted()
                .forEach(System.out::println);
        System.out.println("-----------------");
        employeeList.stream()
                .sorted((e1, e2) -> {
                    if (e1.getAge() == e2.getAge()) {
                        return e1.getName().compareTo(e2.getName());
                    } else {
                        return Integer.compare(e1.getAge(), e2.getAge());
                    }
                })
                .forEach(System.out::println);
    }

    /**
     * 终止操作
     * allMath -检查是否匹配所以元素
     */
    @Test
    public void test7() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        //allMath -检查是否匹配所以元素
        boolean b = employeeList.stream()
                .anyMatch((employee -> employee.getStatus().equals(Employee.Status.BUSY)));
//        anyMath-检查是否至少匹配一个元素
        boolean b2 = employeeList.stream()
                .anyMatch((employee -> employee.getStatus().equals(Employee.Status.BUSY)));
        //noneMath-检查是否没有匹配所有元素
        //findFirst-返回第一个元素
        Optional<Employee> first = employeeList.stream()
                .sorted((e1, e2) -> {
                    return Integer.compare(e1.getAge(), e2.getAge());
                })
                .findFirst();
        //findAny-返回当前流中的任意元素
        Optional<Employee> any = employeeList.stream()
                .filter((e) -> e.getStatus().equals(Employee.Status.FREE))
                .findAny();
        //count-返回流中元素的总个数
        long count = employeeList.stream()
                .count();
        //max-返回流中最大值
        Optional<Employee> max = employeeList.stream()
                .max((e1, e2) -> {
                    return Integer.compare(e1.getAge(), e2.getAge());
                });
        System.out.println(max.get());
        //min
    }

    /**
     * 终止操作 -归约与收集  重要
     * 收集:collect -将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
     */
    @Test
    public void test8() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        //第一次把 x1->0  x2->1    ==>1
        //第二次 x1->1 x2->2
        Integer reduce = list2.stream()
                .reduce(0, (x1, x2) -> x1 + x2);
//        System.out.println(reduce);
        System.out.println("---------规约：reduce 员工年龄总和------------");
        //没有起始值0，所以又可能为null
        Optional<Integer> reduce1 = employeeList.stream()
                .map(Employee::getAge)
                // .reduce((x1, x2) -> x1 + x2);
                .reduce(Integer::sum);
//        System.out.println(reduce1.get());
        System.out.println("---------收集:collect------------");
        List<String> cList = employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        System.out.println(cList);
        Set<String> set = employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
//        System.out.println(set);
        System.out.println("--------HashSet------------");
        HashSet<String> collect = employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("---------总数-----------");
        Long collect1 = employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.counting());
        System.out.println("---------年龄平均值-----------");
        employeeList.stream()
                .collect(Collectors.averagingInt(Employee::getAge));
        System.out.println("---------分组-----------");
        Map<Employee.Status, List<Employee>> collect2 = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getStatus));
//        System.out.println(collect2);
        System.out.println("-------多级分组------------");
        Map<Employee.Status, Map<String, List<Employee>>> collect3 = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy(
                        (e) -> {
                            if (e.getAge() <= 30) {
                                return "青年";
                            } else if (e.getAge() <= 80) {
                                return "中年";
                            } else {
                                return "老年";
                            }
                        })));
        System.out.println(collect3);

    }

    /**
     * 终止操作 -归约与收集  重要
     * 分区/分片
     */
    @Test
    public void test9() {
        Map<Boolean, List<Employee>> collect = employeeList.stream()
                .collect(Collectors.partitioningBy((e) -> e.getAge() > 80));
        System.out.println(collect);

    }

}

