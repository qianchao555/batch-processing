package designpattern.createtype.abstractFactory.factory;

/**
 * @description:工厂创建器，用于获取具体的产品工厂
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.factory
 * @author:xiaoyige
 * @createTime:2021/11/14 19:48
 * @version:1.0
 */
public class FactoryProducer {
    /**
     * @param factoryChoice
     * @return
     */
    public static AbstractFactory getFactory(String factoryChoice) {
        if (factoryChoice.equalsIgnoreCase("shape")) {
            return new ShapeFactory();
        } else if (factoryChoice.equalsIgnoreCase("color")) {
            return new ColorFactory();
        }
        return null;
    }
}
