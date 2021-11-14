package designpattern.abstractFactory.color;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.color
 * @author:xiaoyige
 * @createTime:2021/11/14 19:59
 * @version:1.0
 */
public class RedColor implements Color {
    @Override
    public void fill() {
        System.out.println("红色已经画完。。。");
    }
}
