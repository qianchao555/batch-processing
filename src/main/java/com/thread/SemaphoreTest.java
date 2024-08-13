package com.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @description:30个线程来跑，但是做了限流后，最多有10个线程在同时跑
 * @author:xiaoyige
 * @createTime:2022/7/4 22:50
 * @version:1.0
 */
public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    //将许可证数量10，赋值给state变量，作为锁的数量
    //核心AQS
    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalInt = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    boolean acquireSuccess = false;
                    try {
                        //获取令牌，state状态值-1
                        s.acquire();
                        TimeUnit.SECONDS.sleep(3);
                        acquireSuccess = true;
                        System.out.println("save data" + finalInt);

                    } catch (InterruptedException e) {
                    } finally {
                        //正确释放锁
                        if (acquireSuccess) {
                            s.release();
                        }
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
