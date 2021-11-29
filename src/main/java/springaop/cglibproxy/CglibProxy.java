package springaop.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 代理类
 *
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class CglibProxy implements MethodInterceptor {
    private Object target;
    private Class clazz;

    public CglibProxy(Object o, Class clazz) {
        this.target = o;
        this.clazz = clazz;
    }

    /**
     * 生成代理对象
     * @return
     */
    public Object getNewProxy(){
        //字节码增强器
        //在Java字节码生成之后，对其进行修改，增强其功能，这种方式相当于对应用程序的二进制文件进行修改。通过Enhancer类去实现
        //calzz 类字节码信息
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(clazz);
        //通过回调指定代理类
        enhancer.setCallback(CglibProxy.this);
        return enhancer.create();
    }

    /**
     *
     * @param o Cglib动态生成的 代理类实例
     * @param method 上文中 实体类所调用的被代理的方法引用
     * @param objects 参数值列表
     * @param methodProxy 生成的代理类 对方法的代理引用
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("吃饭之前洗手");
        Object invoke = method.invoke(target, objects);
        System.out.println("饭后洗碗");
        return invoke;
    }
}
