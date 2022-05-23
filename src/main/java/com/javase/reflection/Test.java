package com.javase.reflection;

/**
 * @description:
 * @projectName:batch-processing
 * @author:xiaoyige
 * @createTime:2021/11/4 21:39
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        //通过反射获取类信息
        Class<?> user = Class.forName("com.javase.reflection.User");
        System.out.println(user);
        Class<?> user2 = Class.forName("com.javase.reflection.User");

        Class<?> user3 = Class.forName("com.javase.reflection.User");
        Class<?> user4 = Class.forName("com.javase.reflection.User");
        //一个类在
        System.out.println(user2.hashCode());
        System.out.println(user3.hashCode());
        System.out.println(user4.hashCode());


    }
}
