package com.designpattern.createtype.abstractFactory.shape;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.abstractFactory.shape
 * @author:xiaoyige
 * @createTime:2021/11/14 19:56
 * @version:1.0
 */
public class SquareShape implements Shape {
    @Override
    public void draw() {
        System.out.println("绘制了方形。。。。");
    }
}
