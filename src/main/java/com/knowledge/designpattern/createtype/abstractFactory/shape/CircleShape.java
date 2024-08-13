package com.knowledge.designpattern.createtype.abstractFactory.shape;

/**
 * @description:圆形Shape
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory
 * @author:xiaoyige
 * @createTime:2021/11/14 19:37
 * @version:1.0
 */
public class CircleShape implements Shape {
    @Override
    public void draw() {
        System.out.println("绘制了圆形。。。。。。");
    }
}
