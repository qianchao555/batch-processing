package designpattern.abstractFactory.factory;

import designpattern.abstractFactory.color.Color;
import designpattern.abstractFactory.shape.CircleShape;
import designpattern.abstractFactory.shape.Shape;
import designpattern.abstractFactory.shape.SquareShape;

import java.util.Calendar;

/**
 * @description:图形工厂，专门生产各种图形
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.factory
 * @author:xiaoyige
 * @createTime:2021/11/14 19:41
 * @version:1.0
 */
public class ShapeFactory implements AbstractFactory {
    @Override
    public Shape createShape(String shapeType) {
        if ("circle".equals(shapeType)) {
            return new CircleShape();
        } else if ("square".equals(shapeType)) {
            return new SquareShape();
        }
        Calendar.getInstance();
        return null;
    }

    @Override
    public Color createColor(String colorType) {
        return null;
    }
}
