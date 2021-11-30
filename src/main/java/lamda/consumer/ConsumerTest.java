package lamda.consumer;

import java.util.function.Consumer;

/**
 * @ClassName ConsumerTest
 * @Author qianchao
 * @Date 2021/11/2
 * @Version OPRA V1.0
 **/
public class ConsumerTest {
    public static void main(String[] args) {
        myConsume(5, (x) -> System.out.println(x));
    }

    /**
     *
     * Consumer 消费型接口  void accept(T t);
     * @param n
     * @param c
     */
    public static void myConsume(Integer n, Consumer<Integer> c) {
        c.accept(n);
    }

}
