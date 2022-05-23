package com.designpattern.structured.adapter.demo1.round;

/**
 * 圆钉
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class RoundPeg {
    private double radius;

    public RoundPeg() {}

    public RoundPeg(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
