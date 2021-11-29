package designpattern.createtype.abstractFactory;

import designpattern.createtype.abstractFactory.color.Color;
import designpattern.createtype.abstractFactory.factory.AbstractFactory;
import designpattern.createtype.abstractFactory.factory.FactoryProducer;
import designpattern.createtype.abstractFactory.shape.Shape;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory
 * @author:xiaoyige
 * @createTime:2021/11/14 19:35
 * @version:1.0
 */
public class DemoTest {
    public static void main(String[] args) {
        //1. 获取具体产品的工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("shape");
        //工厂有了，创建具体某种产品
        Shape shape1 = shapeFactory.createShape("circle");
        shape1.draw();
        Shape square = shapeFactory.createShape("square");
        square.draw();

        //1. 获取具体产品的工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("color");
        Color red = colorFactory.createColor("red");
        red.fill();
    }
}
