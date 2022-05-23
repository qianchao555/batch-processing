package com.spring.circleexception;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/4 22:27
 * @version:1.0
 */
@Component
public class BeanCurrentlyInCreationExceptionDemo {
    public static void main(String[] args) {
        //构造方式注入：已经出现循环依赖了，没有办法解决
//        new ServiceA(new ServiceB(new ServiceA()))
        //setter注入方式，不会出现循环依赖问题
        ServiceC serviceC = new ServiceC();
        ServiceD serviceD = new ServiceD();
        serviceC.setServiceD(serviceD);
        serviceD.setServiceC(serviceC);
    }
}
