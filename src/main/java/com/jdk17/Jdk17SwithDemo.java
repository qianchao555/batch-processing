package com.jdk17;

import org.junit.Test;

public class Jdk17SwithDemo {



    @Test
    public  void swithDemo() {
        String str = "hello";
        switch (str) {
            case "hello" -> System.out.println("hello");
            case "world" -> System.out.println("world");
            default -> System.out.println("default");
        }

        //instanceof 类型模式匹配
        Object obj = 12;
        switch (obj){
            case String s -> System.out.println("string");
            default -> System.out.println("default");
        }

    }
}
