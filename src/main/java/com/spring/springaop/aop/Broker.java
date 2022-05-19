package com.spring.springaop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 切面
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
@Component
@Aspect
public class Broker {
    //切入点
    @Pointcut("execution (* com.spring.springaop.aop.Landlord.service())")
    public void aspectPoint(){}
//    @Before("execution (* springaop.Landlord.service())")
//    public void before(){
//        System.out.println("带租客看房");
//        System.out.println("谈钱");
//    }
//
//    @After("execution (* springaop.Landlord.service())")
//    public void after(){
//        System.out.println("交钥匙");
//    }
    //aspectPoint切入点  可以像上面一样写在属性里面
    @Around("aspectPoint()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("前置通知：带租客看房");
        System.out.println("前置通知：谈价格");
        try {
            //返回目标方法返回值
//            return proceedingJoinPoint.proceed();
             proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("后置通知：交钥匙");
        //修改返回
        return "444";
    }
}
