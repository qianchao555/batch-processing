package com.service;

import com.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

    /**
     * 出现异常被捕获不能回滚
     * @return
     */
    @Transactional
    public String transactionInsert() throws Exception {
        int insert = userMapper.insertUser("2");
        log.info("插入了id=2的记录，接下来插入id=3的记录");
        String s = transactionInsert2();
        return s;
    }


    public String transactionInsert2() throws Exception {
        int insert = userMapper.insertUser("3");
        throw  new Exception("Exception异常");
//        int i = 10/0;
//        try{
//            int i = 10/0;
//        }
//        catch (Exception e){
//            log.info("错误信息：{}",e.getMessage());
//        }
//        if(insert!=0){
//            return "虽然加入了Transactional注解，但是出现异常，并且捕获了异常，导致事务失效，依然能插入成功";
//        }else {
//            return "触发回滚机制，插入失败！";
//        }
    }

    /**
     * 出现异常能回滚
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String transactionInsert3(){
        int insert = userMapper.insertUser("5");
        try{
            int i = 1/0;
        }catch (Exception e){
            log.info("错误信息：{}",e.getMessage());
            throw  new RuntimeException("手动抛出异常");
        }
        if(insert!=0){
            return "虽然加入了Transactional注解，但是出现异常，并且捕获，导致事务失效，依然能插入成功";
        }else {
            return "就是出现异常，并且异常被捕获，也会进行回滚，插入失败！";
        }
    }


    /**
     * 出现异常能回滚
     * @return
     */
    @Transactional
    public String transactionInsert4(){
        int insert = userMapper.insertUser("4");
        try{
            int i = 1/0;
        }catch (Exception e){
            log.info("错误信息：{}",e.getMessage());
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //虽然手动回滚了，但是此时insert还是等于1
        if(insert!=0){
            return "虽然加入了Transactional注解，但是出现异常，并且捕获，导致事务失效，依然能插入成功";
        }else {
            return "就是出现异常，并且异常被捕获，也会进行回滚，插入失败！";
        }

    }

}
