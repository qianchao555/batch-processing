package com.nio;

import java.nio.IntBuffer;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/5/4 22:47
 * @version:1.0
 */
public class BasicBuffer {
    public static void main(String[] args) {
        //简单buffer的使用
        //创建一个buffer,大小为5，即：可以存5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        intBuffer.put(10);
        intBuffer.put(11);
        intBuffer.put(12);
        intBuffer.put(13);
        intBuffer.put(14);
        intBuffer.put(15);

        //从buffer读取数据

        //将buffer进行转换，即：读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
