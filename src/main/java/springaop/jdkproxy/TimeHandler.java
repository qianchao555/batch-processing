package springaop.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author qianchao
 * JDK动态代理类
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public class TimeHandler implements InvocationHandler {
    private Object target;

    public TimeHandler(Object target) {
        this.target = target;
    }

    /**
     * 代理实例、调用的方法、方法的参数列表
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("饭前洗手");
        //调用真正的方法
        Object invoke = method.invoke(target, args);
        System.out.println("饭后洗碗。。");
        return invoke;
    }
}
