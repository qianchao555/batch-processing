package com.knowledge.java8.stream;

import java.util.concurrent.RecursiveTask;

/**
 * @author:xiaoyige
 * @createTime:2021/12/13 20:14
 * @version:1.0
 */
public class Demo2 extends RecursiveTask<Integer> {

    private int start;
    private int end;
    private static final int THRESHOLD = 1000;

    public Demo2(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {

        int len = end - start;
        if (len < THRESHOLD) {
            int sum = 0;
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            Demo2 left = new Demo2(start, mid);
            left.fork();//拆分子任务，同时压入任务队列
            Demo2 right = new Demo2(mid + 1, end);
            right.fork();//拆分子任务，同时压入任务队列
            return left.join() + right.join();
        }
    }
}
