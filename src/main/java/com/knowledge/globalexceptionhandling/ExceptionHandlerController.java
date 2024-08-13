package com.knowledge.globalexceptionhandling;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/11/19 22:30
 * @version:1.0
 */
@Controller
public class ExceptionHandlerController {

    @RequestMapping("/testExceptionHandler")
    public String test(){
        throw new RuntimeException("出错啦！");
    }
}
