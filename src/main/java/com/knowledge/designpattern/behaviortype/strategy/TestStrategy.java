package com.knowledge.designpattern.behaviortype.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName TestStrategy
 * @Author qianchao
 * @Date 2022/11/10
 * @Version   V1.0
 **/
@Service
public class TestStrategy {

    @Autowired
    private StrategyContext strategyContext;


    public String test(String strategyKey){
        return strategyContext.dispatcherStrategy("11","22",strategyKey);
    }

}
