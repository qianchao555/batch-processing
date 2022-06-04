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
public class UserServiceUpdateById2 {

    @Autowired
    UserServiceUpdateById3 userServiceUpdateById3;
    @Autowired
    UserDao userDao;

    @Transactional
    public void test() {
        userDao.insert(new User("11", "name11", "程度", 12));
//        try {
//            throw new RuntimeException("插入后报错了");
//        } catch (Exception e) {
//            log.info("异常信息：{}", e.getMessage());
//        }

    }

}
