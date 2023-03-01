package com.jvm;

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

}
