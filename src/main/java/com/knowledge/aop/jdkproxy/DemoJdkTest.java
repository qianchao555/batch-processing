package com.knowledge.aop.jdkproxy;

import java.lang.reflect.Proxy;

/**
 * @ClassName DemoJdkTest
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class DemoJdkTest {
    public static void main(String[] args) {
        //需要增强的类
        DayWorkImpl dayWork=new DayWorkImpl();
        //代理类
        TimeHandler timeHandler=new TimeHandler(dayWork);
        /**
         * Proxy.newProxyInstance：返回代理类对象。创建的代理对象是在JVM运行时动态生成的一个对象，
         * 这个对象不是我们知道的任何一个对象，而是运行时动态生成的，并且命名方式都是以$Proxy这种类型的。看运行结果也看出来 $Proxy0就是实际代理类
         *
         *  dayWork.getClass().getInterfaces()：我们提供给代理对象的接口，这个代理对象就会去实现这个接口，这种情况下，我们当然可以将这个代理对象强制转化成提供的接口类型
         *  timeHandler：调用处理器。一个接口，需要实现InvocationHandler=》实现动态代理
         */
        IDayWork work = (IDayWork) Proxy.newProxyInstance(dayWork.getClass().getClassLoader(), dayWork.getClass().getInterfaces(), timeHandler);
        //此时work为一个代理对象，调用方法时都会去调用调用处理器的invoke方法
        work.breakfast();
        work.lunch();
        work.dinner();
        System.out.println(work.getClass());
    }
}
