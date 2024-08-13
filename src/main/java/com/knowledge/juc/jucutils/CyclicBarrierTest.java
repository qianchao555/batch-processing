package com.knowledge.juc.jucutils;

import java.util.concurrent.CyclicBarrier;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/7/4 21:13
 * @version:1.0
 */
public class CyclicBarrierTest {
    static CyclicBarrier c = new CyclicBarrier(2, new A());
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                }
                try {
//                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(1);
            }
        }).start();
        try {
            c.await();
        } catch (Exception e) {
        }
        System.out.println(2);
    }
    static class A implements Runnable {
        @Override
        public void run() {
            System.out.println(3);
        }
    }

}
