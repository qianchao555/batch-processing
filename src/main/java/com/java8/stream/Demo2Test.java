package com.java8.stream;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/13 21:00
 * @version:1.0
 */
public class Demo2Test {
    public static void main(String[] args) {
        long reduce = LongStream.rangeClosed(0, 100000000L)
                .parallel()
//                .reduce(0, (x1, x2) -> x1 + x2);
                .reduce(0, Long::sum);
        System.out.println(reduce);
    }

    @Test
    public void test1() {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> task = new Demo2(0, 100);
        Integer invoke = pool.invoke(task);
        System.out.println(invoke);
    }
}
