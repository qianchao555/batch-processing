package com.knowledge.juc.threadlocal;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/11/22 21:52
 * @version:1.0
 */
public class ThreadLocalDemo {

    ThreadLocal<ThreadLocalDemo> threadLocal =
            ThreadLocal.withInitial(ThreadLocalDemo::new);

    public static void main(String[] args) {

    }
}
