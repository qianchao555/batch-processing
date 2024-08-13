package com.thread;

/**
 * @ClassName PrintNumAndLetter
 * @Author qianchao
 * @Date 2022/11/30
 * @Version  V1.0
 **/
public class PrintNum implements Runnable {


    Object object;

    public PrintNum(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        printNum();
    }

    public void printNum() {
        synchronized (object) {
            boolean flag = true;
            while (flag) {
                for (int i = 1; i <= 52; i++) {
                    System.out.print(i);
                    if (i % 2 == 0) {
                        try {
                            object.wait();
                            object.notifyAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                flag = false;
            }

        }
    }


//    public synchronized void printLetter() {
//        while (flag) {
//            for (int i = 65; i < 91; i++) {
//                System.out.print((char) i);
//                try {
//                    notify();
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            flag = false;
//        }
//    }
}
