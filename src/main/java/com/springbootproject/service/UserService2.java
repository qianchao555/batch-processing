package com.springbootproject.service;

import com.springbootproject.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:27
 * @version:1.0
 */
@Slf4j
@Service
public class UserService2 {
    @Autowired
    UserMapper userMapper;

    public void transactionInsert0()  {
         userMapper.insertUser("userService2-0");
    }

    /**
     *
     * @return
     */
    public void transactionInsert1()  {
        int insert = userMapper.insertUser("userService2-1");
        int i=10/0;

    }


    public void transactionInsert2()  {
        int insert = userMapper.insertUser("userService2-2");
        try{int i=10/0;}catch (Exception e){}

    }


}
