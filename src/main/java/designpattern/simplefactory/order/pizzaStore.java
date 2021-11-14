package designpattern.simplefactory.order;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory
 * @author:xiaoyige
 * @createTime:2021/11/13 22:53
 * @version:1.0
 */
public class pizzaStore {
    public static void main(String[] args) {
        new OrderPizza(new SimpleFactory());
        System.out.println("推出程序");
    }
}
