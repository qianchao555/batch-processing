package designpattern.createtype.single;

/**
 * @description:饿汉式
 * 优点：类装载时完成实例化。避免了线程同步问题
 * 缺点：未使用这个实例时，造成内存的浪费
 * @projectName:batch-processing
 * @see:designpattern.single
 * @author:xiaoyige
 * @createTime:2021/11/13 21:16
 * @version:1.0
 */
public class SingleStaticVarTest {
    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        System.out.println(instance);
    }

}

class Singleton {
    private final static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }
}
