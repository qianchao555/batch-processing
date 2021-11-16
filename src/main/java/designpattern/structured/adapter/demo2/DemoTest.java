package designpattern.structured.adapter.demo2;

/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/16
 * @Version OPRA V1.0
 **/
public class DemoTest {
    public static void main(String[] args) {
        WildTurkey wildTurkey=new WildTurkey();
        TurkeyAdapter turkeyAdapter=new TurkeyAdapter(wildTurkey);
        //适配器和目标对象交互  目标对象：鸭子
        turkeyAdapter.duckQuack();
        turkeyAdapter.duckFly();
    }
}
