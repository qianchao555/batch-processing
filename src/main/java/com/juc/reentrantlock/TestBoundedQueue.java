package com.juc.reentrantlock;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName TestBoundedQueue
 * @Author qianchao
 * @Date 2022/12/1
 * @Version   V1.0
 **/
public class TestBoundedQueue {
    static BoundedQueue boundedQueue = new BoundedQueue(10);

    public static void main(String[] args) {

        //生产者1s产生一条记录
        for (int i = 0; i < 1; i++) {
            Thread providerThread = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boundedQueue.add("消息");
                }
            }, "生产者线程" + i);
            providerThread.start();

        }

        //消费者10s取一次数据
        for (int i = 0; i < 1; i++) {
            Thread consumerThread = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Object consumer = boundedQueue.consumer();
                    System.out.println("获取到的队列值："+consumer);
                }
            }, "消费者线程" + i);
            consumerThread.start();
        }
    }

}
