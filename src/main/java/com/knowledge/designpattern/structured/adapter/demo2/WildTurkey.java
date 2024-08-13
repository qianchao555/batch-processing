package com.knowledge.designpattern.structured.adapter.demo2;

/**
 * @ClassName WildTurkey
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class WildTurkey implements Turkey {

    @Override
    public void gobble() {
        System.out.println("我是野生火鸡：gobble gobble!!!");
    }

    @Override
    public void fly() {
        System.out.println("我是野生火鸡：I'm flying a short distance!");
    }
}
