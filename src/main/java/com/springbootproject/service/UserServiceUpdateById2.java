package com.springbootproject.service;

import com.springbootproject.dao.UserDao;
import com.springbootproject.entity.User;
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
public class UserServiceUpdateById2 {

    @Autowired
    UserServiceUpdateById3 userServiceUpdateById3;
    @Autowired
    UserDao userDao;



    /**
     *
     * @return
     */
    public void test()  {
        User user=userDao.selectById("2");
        user.setId("2");
        user.setAddr("ById2SetAddress-2");
        userServiceUpdateById3.test();
        userDao.updateById(user);
    }





}
