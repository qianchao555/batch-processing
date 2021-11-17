package designpattern.structured.proxy.dynamicproxy.jdk;

import designpattern.structured.proxy.staticproxy.BuyHouse;
import designpattern.structured.proxy.staticproxy.BuyHouseImpl;

import java.lang.reflect.Proxy;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/11/17
 * @Version OPRA V1.0
 **/
public class Demo {
    public static void main(String[] args) {
        BuyHouse buyHouse = new BuyHouseImpl();
        DynamicProxyHandler dynamicProxyHandler = new DynamicProxyHandler(buyHouse);
        /**
         * ClassLoader loader:指定当前目标对象使用的类加载器,获取加载器的方法是固定的
         * Class<?>[] interfaces:指定目标对象实现的接口的类型,使用泛型方式确认类型
         * InvocationHandler:指定动态处理器，执行目标对象的方法时,会触发事件处理器的方法
         */
        BuyHouse buyHouseProxy = (BuyHouse) Proxy.newProxyInstance(buyHouse.getClass().getClassLoader(), buyHouse.getClass().getInterfaces(), dynamicProxyHandler);
        buyHouseProxy.buyHouse();
    }

}
