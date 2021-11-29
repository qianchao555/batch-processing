package designpattern.structured.proxy.staticproxy;

/**
 * @ClassName BuyHouseProxy
 * @Author qianchao
 * @Date 2021/11/17
 * @Version designpattern V1.0
 **/
public class BuyHouseProxy implements BuyHouse{
    private BuyHouse buyHouse;

    public BuyHouseProxy(BuyHouse buyHouse) {
        this.buyHouse = buyHouse;
    }

    @Override
    public void buyHouse() {
        //之前的逻辑
        System.out.println("买房前准备");
        //调用被代理对象的方法
        buyHouse.buyHouse();
        //之后的逻辑
        System.out.println("买房后装修");
    }
}
