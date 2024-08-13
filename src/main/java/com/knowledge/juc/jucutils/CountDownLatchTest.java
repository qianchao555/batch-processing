package com.knowledge.juc.jucutils;

import java.util.concurrent.CountDownLatch;

/**
 * @description:等待一组线程执行完后再执行
 * @author:xiaoyige
 * @createTime:2022/7/4 20:43
 * @version:1.0
 */
public class CountDownLatchTest {
    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                c.countDown();


                System.out.println(2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                c.countDown();
            }
        }).start();
        c.await();
        System.out.println("3");
    }
}
