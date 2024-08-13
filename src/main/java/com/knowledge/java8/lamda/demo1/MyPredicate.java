package com.knowledge.java8.lamda.demo1;

/**
 * 自定义的一个判断形接口
 * 泛型T ==>表示传递什么类型，返回就是什么
 * 单独T ==>表示传入泛型参数
 * <T> T ==>表示返回值也是T
 *
 * @author:xiaoyige
 * @createTime:2021/12/8 21:55
 * @version:1.0
 */
public interface MyPredicate<T> {
    boolean filter(T t);
}
