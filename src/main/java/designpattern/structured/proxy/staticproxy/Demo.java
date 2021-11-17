package designpattern.structured.proxy.staticproxy;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/11/17
 * @Version OPRA V1.0
 **/
public class Demo {
    public static void main(String[] args) {
        //被代理对象 new BuyHouseImpl
        BuyHouse buyHouse=new BuyHouseImpl();
        buyHouse.buyHouse();

        //代理对象
        BuyHouseProxy buyHouseProxy=new BuyHouseProxy(buyHouse);
        //执行代理对象方法，会调用被代理对象方法
        buyHouseProxy.buyHouse();
    }

}
