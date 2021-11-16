package designpattern.structured.adapter.demo3;

/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/16
 * @Version OPRA V1.0
 **/
public class DemoTest {
    public static void main(String[] args) {
        AC ac=new AC();
        MacAdapter macAdapter=new MacAdapter(ac);
        System.out.println(macAdapter.output20V());
    }

}
