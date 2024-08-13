package com.knowledge.jvm;

import org.junit.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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


    @org.junit.jupiter.api.Test
    public void testString() {
        //new String(“a”) + new String(“b”)
        // 在堆中创建了三个字符串对象（a字符串对象，b字符串对象，ab字符串对象），
        //在堆中用两个new创建了两个String对象，然后通过StringBuilder对象调用append方法进行两次字符串的添加操作，最后用StringBuilder的toString方法进行返回了一个堆中的字符串对象


        // 在堆中的字符串常量池中创建了2个字符串对象（a字符串对象，b字符串对象），一共创建了5个对象

        String s1 = new String("2") + new String("2");//这里编译时常量池中存放的是new String("2") ,并没有生成new String("22")
        s1.intern(); //首先没有发现"22"这个字符串对象，所以复制一份堆对象的引用在字符串常量池中，所以，此时字符串常量池中存放的"22"是s1这个引用
        String s2 = "22";//所以这个22指向的是s1这个引用指向的对象，所以是相同的
        System.out.println(s1 == s2);//true
    }

    @org.junit.jupiter.api.Test
    public void testString2() {
        String s1 = new String("a") + new String("a");
        s1.intern();
        String s2 = "aa";
        System.out.println(s1 == s2);
    }

    @org.junit.jupiter.api.Test
    public void testString3() {
        String hello = "hello";

        String f="hello"+hello;
        String f1="hellohello";
        System.out.println(f==f1);//false


        String hel = "hel";
        String lo = "lo";
        System.out.println(hello == "hel" + "lo");//true

        //String进行运算时，new了StringBuilder对象，然后进行append追加字符串
        //然后调用toString()方法返回堆上new的一个String对象
        System.out.println(hello == hel + lo);//false

        System.out.println(hello=="hel"+lo);//同上采用了StringBuilder
    }

    @org.junit.jupiter.api.Test
    public void testString4() {
        int x=Integer.MAX_VALUE;
        System.out.println(x);
        long temp=(long)Integer.MAX_VALUE+(long)Integer.MAX_VALUE;
        System.out.println(temp);

        System.out.println(x+772271809);

        //-1375211840
//        long count=0;
//        for(int i=-2147483648;i<0;i++){
//            count++;
//            if(i==-1375211840){
//                break;
//            }
//        }
//        System.out.println(count);


    }
    @org.junit.jupiter.api.Test
    public void testString6() {
        Integer i=Integer.MAX_VALUE;
        System.out.println(i);
        long a=(long)i+(long)i;
        System.out.println(a);
    }

    @org.junit.jupiter.api.Test
    public void testString5() {

        String strNum1 = String.valueOf(Long.MAX_VALUE);
        String strNum2 = String.valueOf(Long.MAX_VALUE);

        bigNumAdd("", "34");
    }

    private static void bigNumAdd(String strNum1, String strNum2) {
        int len1 = strNum1.length();
        int len2 = strNum2.length();
        int maxLen = Integer.max(len1, len2);
        StringBuilder sb = new StringBuilder();
        // 进位
        int carry = 0;
        for (int i = 0; i < maxLen; i++) {
            int temp = carry;
            carry = 0;

            if (i < len1) {
                temp += Integer.parseInt(String.valueOf(strNum1.charAt(len1 - 1 - i)));
            }
            if (i < len2) {
                temp += Integer.parseInt(String.valueOf(strNum2.charAt(len2 - 1 - i)));
            }
            if (temp >= 10) {
                temp -= 10;
                carry = 1;
            }
            sb.append(temp);
        }
        if (carry > 0) {
            sb.append(carry);
        }
        System.out.println(String.format("%s + %s = %s", strNum1, strNum2, sb.reverse().toString()));

    }

    @org.junit.jupiter.api.Test
    public void testString7() {
        BigDecimal b1= BigDecimal.valueOf(0);
        Integer maxValue = Integer.MAX_VALUE;
        Integer maxValue2 = Integer.MAX_VALUE;
        Integer maxValue3 = Integer.MAX_VALUE;
        Long longMaxValue = Long.MAX_VALUE;
        //9223372036854775807
        System.out.println(BigDecimal.valueOf(longMaxValue));
        System.out.println(BigDecimal.valueOf(longMaxValue).add(BigDecimal.valueOf(longMaxValue)));
//        System.out.println(b1.add(BigDecimal.valueOf(maxValue)));
    }

}
