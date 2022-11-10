package com.designpattern.behaviortype.strategy;

import org.springframework.stereotype.Component;

/**
 * @ClassName StrategyImpl
 * @Author qianchao
 * @Date 2022/11/10
 * @Version   V1.0
 **/

@Component
public class StrategyImpl {
    public String doSomethingDefault(String arg1,String arg2){
        System.out.println("我是默认的策略");
        return "默认策略";
    }

    public String doSomethingOne(String arg1,String arg2){
        System.out.println("我是策略1");
        return "默认策略1";
    }

    public String doSomethingTwo(String arg1,String arg2){
        System.out.println("我是策略2");
        return "默认策略2";
    }
}
