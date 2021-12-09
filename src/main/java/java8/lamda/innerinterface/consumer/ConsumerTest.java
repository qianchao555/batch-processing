package java8.lamda.innerinterface.consumer;

import java.util.function.Consumer;

/**
 * @ClassName ConsumerTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version java8 V1.0
 **/
public class ConsumerTest {
    public static void main(String[] args) {

        myConsume(5, (x) -> {
            //花了三元钱
            x -= 3;
            System.out.println(x);
        });
    }

    /**
     * Consumer 消费型接口  void accept(T t);
     *
     * @param money
     * @param c
     */
    public static void myConsume(double money, Consumer<Double> c) {
        c.accept(money);
    }

}
