package designpattern.single;

/**
 * @description:懒汉式
 * @projectName:batch-processing
 * @see:designpattern.single
 * @author:xiaoyige
 * @createTime:2021/11/13 21:27
 * @version:1.0
 */
public class LazySingleTest {

}

class LazySingleton {
    //    private LazySingleton instance;
    //valatile多线程时，更新后各个线程能立刻查看到
    private static volatile LazySingleton instance2;

    private LazySingleton() {
    }

    /**
     * 线程不安全
     *
     * @return
     */
//    public LazySingleton getInstance() {
//        if (instance == null) {
//            instance = new LazySingleton();
//        }
//        return instance;
//    }

    /**
     * 双重检查+synchronized +volatile(成员变量之前加)
     *
     * @return
     */
    public static LazySingleton getInstance2() {
        if (instance2 == null) {
            synchronized (LazySingleton.class) {
                if (instance2 == null) {
                    instance2 = new LazySingleton();
                }
            }
        }
        return instance2;
    }

}
