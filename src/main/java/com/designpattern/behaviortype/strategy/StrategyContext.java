package com.designpattern.behaviortype.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @ClassName StrategyContext
 * @Author qianchao
 * @Date 2022/11/10
 * @Version   V1.0
 **/
@Component
public class StrategyContext {

    @Autowired
    private StrategyImpl strategyImpl;

    /**
     * 存放策略
     */
    private Map<String, BiFunction<String,String,String>> strategyMap=new HashMap<>();

    /**
     * 加载所有策略
     */
    @PostConstruct
    public void initStrategies(){
        strategyMap.put("one",(arg1,arg2)->strategyImpl.doSomethingOne(arg1,arg2));
        strategyMap.put("two",(arg1,arg2)->strategyImpl.doSomethingTwo(arg1,arg2));
    }

    /**
     * 分派策略
     * @param args1
     * @param args2
     * @param key
     * @return
     */
    public String dispatcherStrategy(String args1,String args2,String key){
        BiFunction<String, String, String> strategyBiFunction = strategyMap.get(key);
        //没有的策略就走默认策略
        if(Objects.isNull(strategyBiFunction)){
            return strategyImpl.doSomethingDefault(args1,args2);
        }
        return strategyBiFunction.apply(args1,args2);
    }

}
