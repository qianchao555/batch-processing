package com.juc.customlock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;

/**
 * @description:在测试用例中，定义了
 * 工作者线程 Worker，该线程在执行过程中获取锁，当获取锁之后使当前线程睡眠 1 秒
 * （并不释放锁），随后打印当前线程名称，最后再次睡眠 1 秒并释放锁
 *
 * @author:xiaoyige
 * @createTime:2022/7/3 23:27
 * @version:1.0
 */
public class TwinsLockTest {
    /**
     * 预期结果：运行该测试用例，可以看到线程名称成对输出，
     * 也就是在同一时刻只有两个线程能够获取到锁，这表明 TwinsLock 可以按照预期正确工作
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        final Lock lock = new TwinsLock();
        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
        // 启动 10 个线程
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            w.setDaemon(true);
            w.start();
        }
        // 每隔 1 秒换行
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println();
        }
    } }
