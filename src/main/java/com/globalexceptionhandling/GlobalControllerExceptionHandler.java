package com.globalexceptionhandling;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/11/19 22:26
 * @version:1.0
 */

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * controller层抛出异常后，这个方法会去捕捉
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map errorHandler(Exception e){
        Map<String,String> map =new LinkedHashMap<>();
        //自定义错误码和消息
        map.put("errorCode","100");
        map.put("msg",e.getMessage());
        return map;
    }
}
