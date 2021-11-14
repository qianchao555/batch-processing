package designpattern.abstractFactory.factory;

import designpattern.abstractFactory.color.Color;
import designpattern.abstractFactory.color.RedColor;
import designpattern.abstractFactory.shape.Shape;

/**
 * @description:ColorFactory专门生产各种颜色产品
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.factory
 * @author:xiaoyige
 * @createTime:2021/11/14 20:03
 * @version:1.0
 */
public class ColorFactory implements AbstractFactory {
    @Override
    public Shape createShape(String shapeType) {
        return null;
    }

    @Override
    public Color createColor(String colorType) {
        if(colorType.equalsIgnoreCase("red")){
            return new RedColor();
        }
        return null;
    }
}
