package springaop.jdkproxy;

/**
 * @ClassName DayWorkImpl
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public class DayWorkImpl implements IDayWork{

    @Override
    public void breakfast() {
        System.out.println("早饭");
    }

    @Override
    public void lunch() {
        System.out.println("午饭饭");

    }

    @Override
    public void dinner() {
        System.out.println("晚饭饭");

    }
}
