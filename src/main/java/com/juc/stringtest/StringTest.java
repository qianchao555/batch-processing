package com.juc.stringtest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:TODO
 * @ClassName StringTest
 * @Author qianchao
 * @Date 2023/2/28
 * @Version V1.0
 **/
public class StringTest {
    public static AtomicInteger race = new AtomicInteger(0);
    private static final int THREADS_COUNT = 5;

    public static void increase() {
        race.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread[] threads = new Thread[THREADS_COUNT];
//        for (int i = 0; i < THREADS_COUNT; i++) {
//            threads[i] = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 10; i++) {
//                        increase();
//                    }
//                }
//            });
//            threads[i].start();
//        }
//        Thread.sleep(2000);
//        System.out.println(race);
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));
    }

}
