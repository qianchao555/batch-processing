package com.knowledge.designpattern.createtype.simplefactory.pizza;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory
 * @author:xiaoyige
 * @createTime:2021/11/13 22:33
 * @version:1.0
 */
public abstract class Pizza {
    protected String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //准备
    public abstract void prepare();

    //烘烤
    public void bake() {
        System.out.println(name + "baking");
    }

    //切
    public void cut() {
        System.out.println(name + "cuting");
    }

    //打包
    public void bax() {
        System.out.println(name + "boxing");
    }
}
