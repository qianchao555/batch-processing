package jvm;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/12/21
 * @Version V1.0
 **/
public class Demo {
    private static int v=123;  //类加载过程的准备阶段 赋予默认值0，类实例化时赋值123
    private int my; //实例化对象时，分配内存阶段赋予默认值
    public static void main(String[] args) {
        System.out.println(v);
    }
}
