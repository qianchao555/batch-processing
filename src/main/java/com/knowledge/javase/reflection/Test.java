package com.knowledge.javase.reflection;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
/**
 * @description:
 * @projectName:batch-processing
 * @author:xiaoyige
 * @createTime:2021/11/4 21:39
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
//        String[] split = "".split(";");
//        
//        System.out.println(split.length);
//        System.out.println(Arrays.asList(split).contains(""));
//        Map<String,String> staffMap=new HashMap<>();
//        String s = staffMap.get("123");
//        System.out.println(staffMap.get("123")==null?"是null啊":"不是null啊");
        //通过反射获取类信息
//        Class<?> user = Class.forName("com.javase.reflection.User");
//        System.out.println(user);
//        Class<?> user2 = Class.forName("com.javase.reflection.User");
//
//        Class<?> user3 = Class.forName("com.javase.reflection.User");
//        Class<?> user4 = Class.forName("com.javase.reflection.User");
//        //一个类在
//        System.out.println(user2.hashCode());
//        System.out.println(user3.hashCode());
//        System.out.println(user4.hashCode());
        List<Long> x=new ArrayList<>();
        x.add(12L);
        x.add(1245678945L);
        x.add(145989892L);
        x.add(12457897L);
        log.info("没有查询cba状态的函证id为{}",x);

        for (int i = 0; i < 20; i++) {
            System.out.println(i);
        }


    }
}
