package com.knowledge.java8.completablefuture;

import java.util.concurrent.Future;

/**
 * @ClassName TestCompletableFuture
 * @Author qianchao
 * @Date 2022/12/29
 * @Version   V1.0
 **/
public class TestCompletableFuture {
    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");

        long start = System.nanoTime();
        //异步调用
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        // 执行更多任务，比如查询其他商店
        doSomethingElse();

        // 在计算商品价格的同时
        try {
            System.out.println(Thread.currentThread().getName()+"会阻塞在这里，等待获取。。。");
            while (!futurePrice.isDone()){
                System.out.println("异步线程还在执行！");
            }
            double price = futurePrice.get();
            System.out.println("异步线程获取到结果了，我继续执行了-------------");
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

    private static void doSomethingElse() {
        System.out.println(Thread.currentThread().getName()+"我将休息2s,模拟做其他事情花费的时间。");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"其他事情做完了！！");
    }


}
