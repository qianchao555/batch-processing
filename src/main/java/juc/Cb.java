package juc;

/**
 * @ClassName Cb
 * @Author qianchao
 * @Date 2022/5/16
 * @Version OPRA V1.0
 **/
public abstract class Cb {
    public Cb(){}
    abstract void a();
    public void b(){
        System.out.println("抽象类的b方法");
    }

}
