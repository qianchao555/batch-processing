package com.java8.stream.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @ClassName AppleTest
 * @Author qianchao
 * @Date 2022/12/8
 * @Version   V1.0
 **/
public class AppleTest {


    private static List<Apple> appleList = new ArrayList<>();

    static {
        appleList.add(new Apple("green", 123.3));
        appleList.add(new Apple("red", 143.3));
        appleList.add(new Apple("red", 123));
    }


    /**
     * 没用Java8之前
     *
     * @param appleList
     * @return
     */
    public List<Apple> filterApple(List<Apple> appleList) {
        List<Apple> filteredList = new ArrayList<>();
        for (Apple apple : appleList) {
            if ("green".equals(apple.getColor())) {
                filteredList.add(apple);
            }
        }
        return filteredList;
    }

    /**
     * 使用Java8后
     *
     * @param appleList
     * @param predicate
     * @return
     */
    public List<Apple> filterAppleForPredicate(List<Apple> appleList, Predicate<Apple> predicate) {
        List<Apple> filteredList = new ArrayList<>();
        for (Apple apple : appleList) {
            if (predicate.test(apple)) {
                filteredList.add(apple);
            }
        }
        return filteredList;
    }

    /**
     * 将List泛化
     *
     * @param list
     * @param predicate
     * @param <T>
     * @return
     */
    public <T> List<T> filterAppleForPredicate2(List<T> list, Predicate<T> predicate) {
        List<T> filteredList = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }

    /**
     * 断言-苹果颜色是绿色
     *
     * @param apple
     * @return
     */
    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    /**
     * 断言-苹果重量大于130/
     *
     * @param apple
     * @return
     */
    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 130;
    }


    public static void main(String[] args) {


        AppleTest appleTest = new AppleTest();


        List<Apple> apples = appleTest.filterApple(appleList);
        System.out.println(apples);

        //使用Java8后的调用方式1
        List<Apple> apples1 = appleTest.filterAppleForPredicate(appleList, AppleTest::isHeavyApple);
        System.out.println(apples1);

        //使用Java8后的调用方式2
        List<Apple> apples2 = appleTest.filterAppleForPredicate(appleList, (Apple apple) -> "red".equals(apple.getColor()));
        List<Apple> apples3 = appleTest.filterAppleForPredicate(appleList,
                (Apple apple) ->
                        "red".equals(apple.getColor()) || "green".equals(apple.getColor()));
        System.out.println(apples2);


        //将List类型泛化，以满足不同的类型,现在List里面可以放置西瓜、草莓、String、Integer等等了
        appleTest.filterAppleForPredicate2(appleList,(Apple apple)->"red".equals(apple.getColor()));
    }


}
