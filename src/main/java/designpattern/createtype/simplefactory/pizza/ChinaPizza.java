package designpattern.createtype.simplefactory.pizza;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory.pizza
 * @author:xiaoyige
 * @createTime:2021/11/13 23:30
 * @version:1.0
 */
public class ChinaPizza extends Pizza {
    @Override
    public void prepare() {
        System.out.println("中国pizza 准备");
    }
}
