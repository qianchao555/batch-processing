package com.util.annotation.thinkinginjava.mytestannotation;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/1/15 19:46
 * @version:1.0
 */
public class PrintNum {
//    void print123() {
//        System.out.println("123");
//    }

    /**
     * 采用自定义的注解
     */
    @MyTest
    public void print456() {
        System.out.println("456");
    }
}
