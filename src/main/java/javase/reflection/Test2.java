package javase.reflection;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.javase.reflection
 * @author:xiaoyige
 * @createTime:2021/11/4 22:52
 * @version:1.0
 */
public class Test2 {
    public static void main(String[] args) {
        A a=new A();
        System.out.println(A.m);
    }
}
class A{
    public A(){
        System.out.println("A构");
    }
    static {
        System.out.println("A静态块");
        m=100;
    }
    static int m;
}