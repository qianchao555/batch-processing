package com.juc.atomic;

import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.*;

/**
 * @ClassName AtomicTest
 * @Author qianchao
 * @Date 2022/7/4
 * @Version   V1.0
 **/
public class AtomicTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger=new AtomicInteger(0);
        int andIncrement = atomicInteger.getAndIncrement();
        AtomicLong atomicLong;
        AtomicDouble atomicDouble;
        AtomicBoolean atomicBoolean;

        AtomicIntegerArray atomicIntegerArray;
        AtomicLongArray atomicLongArray;
        AtomicDoubleArray atomicDoubleArray;
        AtomicReferenceArray atomicReferenceArray;

        AtomicReference atomicReference;
        BlockingQueue<Future<R>> queue=new LinkedBlockingQueue<>();
    }

}
