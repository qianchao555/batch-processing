package com.jvm;

import com.baomidou.mybatisplus.annotation.TableName;
import org.junit.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName JVMTest
 * @Author qianchao
 * @Date 2022/3/15
 * @Version  V1.0
 **/

public class JVMTest implements WebMvcConfigurer {
    static class A{}
    public static void main(String[] args) {
        List<Object> list=new ArrayList<>();
        int a=1;
        while(a==1){
            list.add(new A());
        }
    }

    /**
     * 测试强软弱虚引引用
     */
    @Test
    public void testRef() throws InterruptedException {
        WeakReference<Object> o=new WeakReference<>(new Object());
        System.out.println(o.get());
        System.gc();
        //给GC一些时间，保证GC执行完成
        TimeUnit.SECONDS.sleep(2);
        if(o.get()==null){
            System.out.println("弱引用指向的对象在发生GC时,被回收了");
        }
    }


    /**
     * 测试 对象优先分配Eden区
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:+UseConcMarkSweepGC   启用CMS垃圾收集器观测与默认垃圾收集器的区别
     */
    @Test
    public void testAllocation() throws InterruptedException {
        int _1MB = 1024 * 1024;
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB];
        Thread.sleep(3000);
    }

    static abstract class Human {
    }
    static class Man extends Human {
    }
    static class Woman extends Human {
    }

    public void sayHello(Human guy) {
        System.out.println("hello,guy!");
    }
    public void sayHello(Man guy) {
        System.out.println("hello,gentleman!");
    }
    public void sayHello(Woman guy) {
        System.out.println("hello,lady!");
    }
    @Test
    public void testStaticDispath(){


        Human man = new Man();
        Human woman = new Woman();
        JVMTest sr = new JVMTest();
        sr.sayHello((Man) man);
        sr.sayHello((Woman) woman);
    }

}
