package designpattern.structured.adapter.demo3;

/**
 * 中间类，空实现所有方法，这是一个抽象类
 * @Author qianchao
 * @Date 2021/11/16
 * @Version OPRA V1.0
 **/
public abstract class DefaultAdapter implements IDC{
    @Override
    public int output5V() {
        return 0;
    }

    @Override
    public int output110V() {
        return 0;
    }

    @Override
    public int output20V() {
        return 0;
    }
}
