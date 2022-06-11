//package com.springbootproject.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.springbootproject.service.UserService;
//
///**
// * @description:
// * @author:xiaoyige
// * @createTime:2022/5/19 21:10
// * @version:1.0
// */
//@Controller
//@RequestMapping("/test")
//public class TrasactionController {
//
//    @Autowired
//    UserService userService;
//
//    @ResponseBody
//    @RequestMapping("/transactionTest1")
//    public String toRegisterSuccess() throws Exception {
//
//        return userService.transactionInsert1();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest3")
//    public String toRegisterSuccess3() throws Exception {
//
//        return userService.transactionInsert3();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest5")
//    public String toRegisterSuccess5() throws Exception {
//
//        return userService.transactionInsert5();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest7")
//    public String toRegisterSuccess7() throws Exception {
//
//        return userService.transactionInsert7();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest9")
//    public void toRegisterSuccess9() throws Exception {
//
//          userService.transactionInsert9();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest10")
//    public void toRegisterSuccess10() throws Exception {
//
//          userService.transactionInsert10();
//
//    }
//
//
//    @ResponseBody
//    @RequestMapping("/transactionTest11")
//    public String toRegisterSuccess11() throws Exception {
//
//        return userService.transactionInsert11();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest12")
//    public String toRegisterSuccess12() throws Exception {
//
//        return userService.transactionInsert12();
//
//    }
//
//    @ResponseBody
//    @RequestMapping("/transactionTest13")
//    public String toRegisterSuccess13() throws Exception {
//
//        return userService.transactionInsert13();
//
//    }
//
//
//    @ResponseBody
//    @RequestMapping("/myUpdate")
//    public void myUpdate() throws Exception {
//
//        userService.myUpdateTest();
//
//    }
//
//}
