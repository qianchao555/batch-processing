package com.knowledge.juc.threaddemo;

/**
 * @ClassName PrintNumAndLetter
 * @Author qianchao
 * @Date 2022/11/30
 * @Version  V1.0
 **/
public class PrintLetter implements Runnable {

    Object object;

    public PrintLetter(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        printLetter();
    }


    public void printLetter() {
        synchronized (object) {
            boolean flag = true;
            while (flag) {
                for (int i = 65; i < 91; i++) {
                    System.out.println((char) i);
                    try {
                        object.notifyAll();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                flag = false;
            }
        }
    }
}
