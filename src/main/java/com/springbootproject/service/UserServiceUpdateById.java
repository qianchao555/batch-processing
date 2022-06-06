package com.springbootproject.service;

import com.springbootproject.dao.UserDao;
import com.springbootproject.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:27
 * @version:1.0
 */
@Slf4j
@Service
public class UserServiceUpdateById {
    @Autowired
    UserServiceUpdateById2 userServiceUpdateById2;

    @Autowired
    UserDao userDao;



    /**
     *
     * @return
     */
    @Transactional
    public void test1()  {
        try {

        userServiceUpdateById2.test();
        }catch (Exception ex){
            log.info("捕获异常：{}",ex.getMessage());
        }
        userDao.insert(new User("12","name12","程度",12));
    }





}
