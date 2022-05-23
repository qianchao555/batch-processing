package com.designpattern.createtype.simplefactory.pizza;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory
 * @author:xiaoyige
 * @createTime:2021/11/13 22:35
 * @version:1.0
 */
public class CheesePizza extends Pizza {
    @Override
    public void prepare() {
        System.out.println("制作奶酪披萨 准备原材料");
    }
}
