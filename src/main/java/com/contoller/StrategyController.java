package com.contoller;

import com.designpattern.behaviortype.strategy.TestStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName StrategyController
 * @Author qianchao
 * @Date 2022/11/10
 * @Version   V1.0
 **/

@RestController
public class StrategyController {
    @Autowired
    private TestStrategy testStrategy;

    @PostMapping("/testStrategy")
    public String t(String strategyKey){
        return testStrategy.test(strategyKey);
    }
}
