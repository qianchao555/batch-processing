package com.java8.lamda.demo1;

/**
 * 传入参数t1,t2
 * 返回值R
 *
 * @author:xiaoyige
 * @createTime:2021/12/9 20:51
 * @version:1.0
 */
public interface MyFunction<T, R> {
    R handler(T t1, T t2);
}
