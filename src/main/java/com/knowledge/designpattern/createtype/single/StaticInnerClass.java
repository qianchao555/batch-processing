package com.knowledge.designpattern.createtype.single;

/**
 * @description:静态内部类实现
 * @projectName:batch-processing
 * @see:designpattern.single
 * @author:xiaoyige
 * @createTime:2021/11/13 21:50
 * @version:1.0
 */
public class StaticInnerClass {

}

class StaticClass {
    private StaticClass() {
    }

    /**
     * 静态内部类 StaticClass装载时，SingleInstance不会装载
     *
     */
    private static class SingleInstance {
        private static final StaticClass INSTANCE = new StaticClass();
    }

    public StaticClass getInstance() {
        //调用时，会去装载SingleInstance
        //装载时，是线程安全的
        return SingleInstance.INSTANCE;
    }
}
