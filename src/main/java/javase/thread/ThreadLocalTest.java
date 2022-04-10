package javase.thread;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/4/10 22:53
 * @version:1.0
 */
public class ThreadLocalTest {
    private static final ThreadLocal<Integer> THREAD_LOCAL_NUM=new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return 0;
        }
    };

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    THREAD_LOCAL_NUM.set(10);
                    add();
                }
            }).start();
        }
    }

    private static void add() {
        for (int i = 0; i < 5; i++) {
            Integer integer = THREAD_LOCAL_NUM.get();
            integer+=1;
            THREAD_LOCAL_NUM.set(integer);
            System.out.println(Thread.currentThread().getName()+"  "+integer );
        }
    }
}


