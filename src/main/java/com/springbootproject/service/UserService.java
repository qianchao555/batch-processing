package com.springbootproject.service;

import com.springbootproject.mapper.UserMapper;
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
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService2 userService2;

    /**
     * 事务方法调用 同一个类非事务方法，事务方法报错且不捕获，会导致事务回滚不往数据库插入数据
     *
     * @return
     */
    @Transactional
    public String transactionInsert1() throws Exception {
        int insert = userMapper.insertUser("userService1-1");
        transactionInsert2();
        int i = 10 / 0;
        return "事务方法调用 同一个类非事务方法，事务方法报错且不捕获";
    }

    private void transactionInsert2() throws Exception {
        userMapper.insertUser("userService1-2");
    }

    /**
     * 事务方法调用 同一个类非事务方法，事务方法报错且捕获异常，不会回滚会往数据库插入数据
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String transactionInsert3() throws Exception {
        int insert = userMapper.insertUser("userService1-3");
        transactionInsert4();
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            log.info("发生异常：{}", e.getMessage());
        }
        return "事务方法调用 同一个类非事务方法，事务方法报错且捕获，不会回滚会往数据库插入数据";
    }

    private void transactionInsert4() throws Exception {
        userMapper.insertUser("userService1-4");
    }

    /**
     * 事务方法调用同一个类非事务方法，非事务方法报错不捕获
     *
     * @return
     */
    @Transactional
    public String transactionInsert5() throws Exception {
        int insert = userMapper.insertUser("userService1-5");
        transactionInsert6();

        return "事务方法调用 同一个类非事务方法，非事务方法报错且不捕获，会回滚，不往数据库插入数据";
    }

    private void transactionInsert6() throws Exception {
        userMapper.insertUser("userService1-6");
        int i = 10 / 0;
    }

    /**
     * 事务方法调用同一个类事务方法，非事务方法报错捕获异常
     * <p>
     * 不会回滚
     *
     * @return
     */
    @Transactional
    public String transactionInsert7() throws Exception {
        int insert = userMapper.insertUser("userService1-7");
        transactionInsert8();

        return "事务方法调用 同一个类非事务方法，非事务方法报错且捕获异常";
    }

    private void transactionInsert8() throws Exception {
        int insert = userMapper.insertUser("userService1-8");
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            log.info("发生异常：{}", e.getMessage());
        }
    }

    /**
     * 事务方法调用另一个类的非事务方法，事务方法报错且不捕获异常
     * <p>
     * 会回滚
     *
     * @return
     */
    @Transactional
    public void transactionInsert9() {
        int insert = userMapper.insertUser("userService1-9");
        userService2.transactionInsert0();
        int i = 10 / 0;
    }

    /**
     * 事务方法调用另一个类的非事务方法，事务方法报错且捕获运行时异常
     * <p>
     * 没有指定rollbackfor的类型，不会回滚
     *
     * @return
     */
    @Transactional
    public void transactionInsert10() {
        userMapper.insertUser("userService1-10");
        userService2.transactionInsert0();
        try {
            int i = 5 / 0;
        } catch (Exception e) {
            log.info("发生异常");
        }
    }

    /**
     * 事务方法调用另一个类的非事务方法，事务方法报错非事务方法不报错
     * <p>
     *
     * 事务方法捕获异常，不会回滚
     *
     * @return
     */
    @Transactional
    public String transactionInsert11() {
        int insert = userMapper.insertUser("userService1-11");
        userService2.transactionInsert0();
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            log.info("发生异常");
        }
        return "事务方法调用另一个类的非事务方法，事务方法报错且不捕获异常";
    }

    /**
     * 事务方法调用另一个类的非事务方法，非事务方法报错
     *
     * 都不捕获异常，会回滚
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String transactionInsert12() throws Exception {
        int insert = userMapper.insertUser("userService1-12");
        userService2.transactionInsert1();
        return "事务方法调用另一个类的非事务方法，非事务方法报错且不捕获异常";
    }

    /**
     * 事务方法调用另一个类的非事务方法，非事务方法报错且捕获异常
     * <p>
     *
     * 非事务方法捕获了异常，不会回滚
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String transactionInsert13() throws Exception {
        int insert = userMapper.insertUser("userService1-13");
        userService2.transactionInsert2();
        return "事务方法调用另一个类的非事务方法，非事务方法报错且捕获异常";
    }






    /**
     *
     * @return
     */
    public void accaTest()   {

        userService2.accaTest2();

        log.info("test2返回test了");

//        userMapper.updateUser("accatest","qcTest2->1");

    }

}
