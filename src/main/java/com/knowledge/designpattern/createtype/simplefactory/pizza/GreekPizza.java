package com.knowledge.designpattern.createtype.simplefactory.pizza;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory
 * @author:xiaoyige
 * @createTime:2021/11/13 22:37
 * @version:1.0
 */
public class GreekPizza extends Pizza {
    @Override
    public void prepare() {
        System.out.println("希腊披萨 准备原材料");
    }
}
