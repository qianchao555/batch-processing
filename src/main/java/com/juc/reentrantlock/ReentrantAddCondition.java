package com.juc.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ReentrantAddCondition
 * @Author qianchao
 * @Date 2022/12/1
 * @Version   V1.0
 **/
public class ReentrantAddCondition {
    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition = reentrantLock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ReentrantAddCondition reentrantAddCondition =new ReentrantAddCondition();
        reentrantAddCondition.test();
    }

    public void test() throws InterruptedException {

        Thread thread = new Thread(() -> {
            try {
                //休眠5s,让main线程先进行唤醒
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                reentrantLock.lock();
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + "开始start!");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + "被唤醒!");
            } finally {
                reentrantLock.unlock();
            }
        });
        thread.setName("t1线程");
        thread.start();

        //main线程休眠1s,main线程先进行condition的唤醒，导致signal在awit之前，最终t1线程不能唤醒程序不能终止一直处于阻塞状态

        //Object的wait/notify
        //Lock+Condition 的await/signal都存在这个问题
        TimeUnit.SECONDS.sleep(1);

        try {
            reentrantLock.lock();
            condition.signal();
        } finally {
            reentrantLock.unlock();
        }

        System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + "执行完毕!");

    }


}
