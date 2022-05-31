package com.springbootproject.controller;

import com.springbootproject.service.UserServiceUpdateById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/30 19:52
 * @version:1.0
 */

@Controller
@RequestMapping("/testUpdateById")
public class updateByIdTest {
    @Autowired
    UserServiceUpdateById userServiceUpdateById;

    @ResponseBody
    @RequestMapping("/test1")
    public void toRegisterSuccess() {

         userServiceUpdateById.test1();

    }
}
