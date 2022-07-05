package com.juc.executor;

import java.util.concurrent.*;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/7/5 21:00
 * @version:1.0
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Executors.newCachedThreadPool();
        Object result=null;
        Future<Object> future=new FutureTask<Object>(() -> System.out.println("知识的力量"), result);
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2,10,2, TimeUnit.SECONDS,new DelayQueue());
        Future<?> future1 = threadPoolExecutor.submit(() -> {
            System.out.println("知识的力量2");
        });
        System.out.println(future1.get());
    }
}
