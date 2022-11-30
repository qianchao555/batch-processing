package com.juc.threaddemo;

/**
 * @ClassName ExchangeExcute
 * @Author qianchao
 * @Date 2022/11/30
 * @Version  V1.0
 **/
public class ExchangeExcute {
    public static void main(String[] args) {

        //作为线程的对象锁资源
        Object object = new Object();


        //实现两个线程按照一定的顺序执行  12A34B56C....
        new Thread( new PrintNum(object)).start();
        new Thread(new PrintLetter(object)).start();

    }
}
