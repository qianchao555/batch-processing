package com.designpattern.structured.proxy.dynamicproxy.cglib;

import com.designpattern.structured.proxy.staticproxy.BuyHouse;
import com.designpattern.structured.proxy.staticproxy.BuyHouseImpl;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/11/17
 * @Version designpattern V1.0
 **/
public class Demo {
    public static void main(String[] args) {
        //被代理对象
        BuyHouse buyHouse=new BuyHouseImpl();
        //代理对象
        CglibProxy cglibProxy=new CglibProxy();
        BuyHouseImpl buyHouseCglibProxy= (BuyHouseImpl) cglibProxy.getInstance(buyHouse);
        buyHouseCglibProxy.buyHouse();

    }
}
