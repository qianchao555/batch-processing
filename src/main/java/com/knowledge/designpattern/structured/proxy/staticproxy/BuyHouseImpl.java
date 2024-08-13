package com.knowledge.designpattern.structured.proxy.staticproxy;

/**
 * @ClassName BuyHouseImpl
 * @Author qianchao
 * @Date 2021/11/17
 * @Version designpattern V1.0
 **/
public class BuyHouseImpl implements BuyHouse {
    @Override
    public void buyHouse() {
        System.out.println("我要买房,我是被代理对象的方法。");
    }
}
