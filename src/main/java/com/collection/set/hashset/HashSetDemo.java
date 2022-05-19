package com.collection.set.hashset;

import java.util.HashSet;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/19 21:36
 * @version:1.0
 */
public class HashSetDemo {
    public static void main(String[] args) {
        HashSet hashSet=new HashSet();
        hashSet.add(new Employee("milan",18));
        hashSet.add(new Employee("jack",22));

        //重写了Employee的hash、equals方法，这个不会添加进set里面
        hashSet.add(new Employee("milan",18));
        System.out.println(hashSet);
    }
}
