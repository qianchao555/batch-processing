package com.java8;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @ClassName DishTest
 * @Author qianchao
 * @Date 2022/12/9
 * @Version   V1.0
 **/
public class DishTest {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.DishType.MEAT),
                new Dish("beef", false, 700, Dish.DishType.MEAT),
                new Dish("chicken", false, 400, Dish.DishType.MEAT),
                new Dish("french fries", true, 530, Dish.DishType.OTHER),
                new Dish("rice", true, 350, Dish.DishType.OTHER),
                new Dish("season fruit", true, 120, Dish.DishType.OTHER),
                new Dish("pizza", true, 550, Dish.DishType.OTHER),
                new Dish("prawns", false, 300, Dish.DishType.FISH),
                new Dish("salmon", false, 450, Dish.DishType.FISH));


        //获取菜肴的名称，并连接起来
//        String collect2 = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
//        System.out.println(collect2);

        List<String> collect1 =
                //建立流
                menu.stream()

                        //建立操作流水线：下列每一个操作都会产生一个流，这个这些流就会接成一条流水线，于是，可以看作是对源的查询

                        .filter(x -> x.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        //处理流水线，将流转换为其他形式并返回结果，它返回的不是流
                        //在此前：不会有任何结果产生
                        .collect(Collectors.toList());
        System.out.println(collect1);

//        Map<Dish.DishType, List<Dish>> collect = menu.stream().collect(groupingBy(Dish::getDishType));
//        for (Map.Entry<Dish.DishType, List<Dish>> dishTypeListEntry : collect.entrySet()) {
//            System.out.print(dishTypeListEntry.getKey()+":");
//            System.out.print(dishTypeListEntry.getValue());
//            System.out.println();
//            System.out.println();
//        }

        //reduce归约
//        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
//        Integer reduce = numbers1.stream().reduce(1, (a, b) -> a * b);
//        System.out.println(reduce);


//        //映射为1 3，1 4，2 3，2 4。。。。
//        List<Integer> numbers2 = Arrays.asList(3, 4);
//
//        List<Integer[]> collect = numbers1.stream()
//                .flatMap(
//                        i -> numbers2.stream()
//                                .map(j -> new Integer[]{i, j}))
//                .collect(Collectors.toList());
//        collect.forEach(x->{
//            Arrays.stream(x).forEach(System.out::print);
//            System.out.println();
//        });

    //分组统计个数
        System.out.print("更加类型统计个数：");
        Map<Dish.DishType, Long> collect = menu.stream()
                .collect(groupingBy(Dish::getDishType, counting()));
        System.out.println(collect);

    }


}
