//package com.springbootproject.controller;
//
//import com.springbootproject.service.Log4jLogstashService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @description:
// * @author:xiaoyige
// * @createTime:2022/6/25 17:35
// * @version:1.0
// */
//
//@Slf4j
//@RestController
//public class Log4jLogstashController {
//    @Autowired
//    Log4jLogstashService log4jLogstashService;
//
//    @GetMapping("/log4jLogstashTest")
//    public String showLog() {
//        log.warn("555warn日志测试3,啦啦啦啦,lalala");
//        log.info("555info日志测试3,啦啦啦啦,lalala");
//        log.debug("555debug日志测试3,啦啦啦啦,lalala");
////        log4jLogstashService.logPrint();
//        System.out.println("sysout能打印出来吗？");
//        return "ok";
//
//    }
////    @GetMapping("/testES")
////    public void testEs(){
////        log4jLogstashService.testEs();
////    }
//}
