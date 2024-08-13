package com.knowledge.designpattern.structured.adapter.demo1.round;

/**
 * 圆孔
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class RoundHole {
    private double radius;//半径

    public RoundHole(double radius){
        this.radius=radius;
    }
    public double getRadius(){
        return radius;
    }
    //圆孔 适合 圆钉
    public boolean fits(RoundPeg peg){
        return this.getRadius()>=peg.getRadius();
    }
}
