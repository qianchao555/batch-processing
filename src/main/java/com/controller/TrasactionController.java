package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.service.UserService;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:10
 * @version:1.0
 */
@Controller
@RequestMapping("/test")
public class TrasactionController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("/transactionTest")
    public String toRegisterSuccess() throws Exception {

        String s = userService.transactionInsert();
        return s;

    }

    @ResponseBody
    @RequestMapping("/transactionTest2")
    public String test2() {

        return userService.transactionInsert3();

    }


    @ResponseBody
    @RequestMapping("/transactionTest4")
    public String test4() {

        return userService.transactionInsert4();

    }
}
