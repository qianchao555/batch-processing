package designpattern.structured.proxy.dynamicproxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CglibProxy
 * @Author qianchao
 * @Date 2021/11/17
 * @Version designpattern V1.0
 **/
public class CglibProxy implements MethodInterceptor {
    //目标对象
    private Object target;

    //获取目标对象的字节码信息
    public Object getInstance(Object target){
        this.target=target;
        //增强
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("买房前的操作。。。");
        Object invoke = method.invoke(target, objects);
        System.out.println("买房后的-----------");
        return invoke;
    }
}
