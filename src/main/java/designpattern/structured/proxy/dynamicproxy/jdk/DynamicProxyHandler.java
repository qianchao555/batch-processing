package designpattern.structured.proxy.dynamicproxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态处理器
 * @Author qianchao
 * @Date 2021/11/17
 * @Version OPRA V1.0
 **/
public class DynamicProxyHandler implements InvocationHandler {
    //需要代理的目标对象
    private Object target;

    public DynamicProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("买房之前准备，Jdk动态代理，调用目标对象方法之前的相关逻辑都可以在这里处理。");
        Object invoke = method.invoke(target, args);
        System.out.println("买房后装修。Jdk动态代理，调用目标对象方法之后的相关逻辑都可以在这里处理。");
        return invoke;
    }
}
