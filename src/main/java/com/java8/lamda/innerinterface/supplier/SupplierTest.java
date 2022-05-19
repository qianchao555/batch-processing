package com.java8.lamda.innerinterface.supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 供给型接口
 * 什么都不用传入，但是会返回东西
 *
 * @Author qianchao
 * @Date 2021/11/2
 * @Version java8 V1.0
 **/
public class SupplierTest {
    public static void main(String[] args) {
        //产生6个随机整数
        List<Integer> numList = getNumList(6, () -> (int) (Math.random() * 100));
        numList.forEach(System.out::println);
    }

    /**
     * 供给型接口
     * Supplier<T> T get();
     * 产生指定个数的整数，放入集合中
     *
     * @param x
     * @param supplier
     * @return
     */
    public static List<Integer> getNumList(int x, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            //get方法得到数字
            list.add(supplier.get());
        }
        return list;
    }

}
