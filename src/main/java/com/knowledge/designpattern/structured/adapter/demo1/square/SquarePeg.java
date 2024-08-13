package com.knowledge.designpattern.structured.adapter.demo1.square;

/**
 * 方钉
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class SquarePeg {
    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public double getSquare() {
        double result;
        result = Math.pow(this.width, 2);
        return result;
    }
}
