package com.java8.completablefuture;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName Shop
 * @Author qianchao
 * @Date 2022/12/29
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
public class Shop {
    private String name;

    public CompletableFuture<Double> getPriceAsync(String product) {
//        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//        new Thread(
//                () -> {
//                    double price = calculatePrice(product);
//                    futurePrice.complete(price);
//                }
//        ,"futureThread").start();
//        return futurePrice;

        //或者supplyAsync：该对象完成异步执行后会读取调用生产者方法的返回值
        CompletableFuture<Double> futurePrice=CompletableFuture.supplyAsync(
                ()->calculatePrice(product)
        );
        return futurePrice;
    }

    private double calculatePrice(String product) {
        System.out.println(Thread.currentThread().getName()+"异步线程：我还在计算价格，模拟花费5s");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"异步线程：价格计算完了。。");
        return 11.1;
    }

}
