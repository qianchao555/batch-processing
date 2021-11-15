package springaop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * 切面
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
@Component
@Aspect
public class Broker {
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
    @Around("execution (* springaop.Landlord.service())")
    public void around(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("前置通知：带租客看房");
        System.out.println("前置通知：谈价格");
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("后置通知：交钥匙");
    }
}
