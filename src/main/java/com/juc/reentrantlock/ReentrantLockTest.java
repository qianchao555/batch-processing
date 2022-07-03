package com.juc.reentrantlock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/7/2 20:31
 * @version:1.0
 */
public class ReentrantLockTest{
    public static void main(String[] args) {
        Lock lock=new ReentrantLock(false);
        lock.lock();
        lock.tryLock();
        lock.unlock();
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        reentrantReadWriteLock.readLock().lock();
        reentrantReadWriteLock.writeLock();

        Condition condition = lock.newCondition();
        LockSupport.park();
        condition.signal();
        AtomicInteger a=new AtomicInteger(1);
        a.incrementAndGet();
    }



}
