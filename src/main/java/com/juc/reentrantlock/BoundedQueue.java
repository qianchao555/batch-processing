package com.juc.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有界阻塞队列
 *
 * @ClassName BoundedQueue
 * @Author qianchao
 * @Date 2022/12/1
 * @Version   V1.0
 **/
public class BoundedQueue<T> {
    //容量
    private Object[] items;

    private int addIndex;

    private int removeIndex;

    //当前队列中的数量
    private int currentCount;


    private Lock lock = new ReentrantLock();

    //不为空才能操作
    private Condition notEmpty = lock.newCondition();

    //不为满才能操作
    private Condition notFull = lock.newCondition();


    public BoundedQueue(int size) {
        items = new Object[size];
    }

    /**
     * 队列中添加一个元素，如果满了，则添加线程进入等待状态，直到有空位
     *
     * @param t
     */
    public void add(T t) {
        lock.lock();
        try {
            //队列满了
            while (items.length == currentCount) {
                System.out.println("队列已经满了" + Thread.currentThread().getName() + "：等待中");
                notFull.await();
            }
            items[addIndex] = t;
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            ++currentCount;
            System.out.println(Thread.currentThread().getName()+"生产了一条消息");

            //通知消费者线程，可以来消费了
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费者来获取
     */
    public T consumer() {
        lock.lock();
        try {
            while (currentCount == 0) {
                System.out.println("队列是空的"+Thread.currentThread().getName()+"等待生成中，阻塞住了");
                notEmpty.await();
            }
            Object item = items[removeIndex];
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --currentCount;
            System.out.println(Thread.currentThread().getName()+"消费了一条消息");

            //通知生产者生产
            notFull.signal();
            return (T) item;

        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
        return null;
    }
}
