package com.knowledge.designpattern.createtype.abstractFactory.factory;

import com.knowledge.designpattern.createtype.abstractFactory.color.Color;
import com.knowledge.designpattern.createtype.abstractFactory.color.RedColor;
import com.knowledge.designpattern.createtype.abstractFactory.shape.Shape;

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
