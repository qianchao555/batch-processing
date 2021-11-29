package designpattern.structured.adapter.demo2;

/**
 * @ClassName TurkeyAdapter   对象的适配器模式
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class TurkeyAdapter implements Duck {
    //将火鸡包装进适配器  适配者对象
    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }


    @Override
    public void duckQuack() {
        turkey.gobble();
    }

    @Override
    public void duckFly() {
        turkey.fly();
    }
}
