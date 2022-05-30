//package com.springbootproject.service;
//
//import com.springbootproject.mapper.UserMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * @description:
// * @author:xiaoyige
// * @createTime:2022/5/19 21:27
// * @version:1.0
// */
//@Slf4j
//@Service
//public class UserService2 extends UserService3{
//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    UserService3 userService3;
//
//    public void transactionInsert0()  {
//         userMapper.insertUser("userService2-0");
//    }
//
//    /**
//     *
//     * @return
//     */
//    public void transactionInsert1()  {
//        int insert = userMapper.insertUser("userService2-1");
//        int i=10/0;
//
//    }
//
//
//    public void transactionInsert2()  {
//        int insert = userMapper.insertUser("userService2-2");
//        try{int i=10/0;}catch (Exception e){}
//
//    }
//
//
//    @Transactional(rollbackFor = Exception.class)
//    public void accaTest2()  {
//        log.info("进入test2了");
//
//        accaTest3();
//
//        log.info("test3->返回到test2了");
//
//        userMapper.updateUserIdName("accatest","qcTest2");
//
//    }
//
//
//}
