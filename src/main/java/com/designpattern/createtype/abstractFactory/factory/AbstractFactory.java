package com.designpattern.createtype.abstractFactory.factory;

import com.designpattern.createtype.abstractFactory.color.Color;
import com.designpattern.createtype.abstractFactory.shape.Shape;

/**
 * @description:抽象工厂接口
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.factory
 * @author:xiaoyige
 * @createTime:2021/11/14 19:39
 * @version:1.0
 */
public interface AbstractFactory {
    /**
     * Shape产品
     * @param shapeType
     * @return Shape
     */
    Shape createShape(String shapeType);

    /**
     * Color产品
     * @param colorType
     * @return Color
     */
    Color createColor(String colorType);
}
