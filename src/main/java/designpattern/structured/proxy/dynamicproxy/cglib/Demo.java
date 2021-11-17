package designpattern.structured.proxy.dynamicproxy.cglib;

import designpattern.structured.proxy.staticproxy.BuyHouse;
import designpattern.structured.proxy.staticproxy.BuyHouseImpl;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/11/17
 * @Version OPRA V1.0
 **/
public class Demo {
    public static void main(String[] args) {
        BuyHouse buyHouse=new BuyHouseImpl();
        CglibProxy cglibProxy=new CglibProxy();
        BuyHouseImpl buyHouseCglibProxy= (BuyHouseImpl) cglibProxy.getInstance(buyHouse);
        buyHouseCglibProxy.buyHouse();

    }
}
