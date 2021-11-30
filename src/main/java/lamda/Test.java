package lamda;

import java.util.Arrays;
import java.util.List;

public class Test {
    /**
     * @ClassName Test
     * @Author qianchao
     * @Date 2021/11/1
     * @Version OPRA V1.0
     **/
    public static void main(String[] args) {
        TestStream<String> stream=new TestStream<>();
        List list= Arrays.asList("111","22","3");
        stream.setList(list);
        ConsumerInterface consumerInterface=(a)->{
            return (int) a;
        };
        System.out.println(consumerInterface.accept(200));
//        stream.myForEach(
//                string-> {
//            string+="qc";
//            System.out.println(string);
//            return 2;
//        });
    }
}

class TestStream<T> {
    private List<T> list;

//    public void myForEach(ConsumerInterface<T> consumerInterface) {
//        for (T t : list) {
//            consumerInterface.accept(t);
//        }
//    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
