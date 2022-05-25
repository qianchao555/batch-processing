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
public abstract class UserService3 {
    @Autowired
    UserMapper userMapper;



//    @Transactional(propagation = Propagation.REQUIRES_NEW )
    protected void accaTest3()  {
        log.info("进入test3了,它的add为：test3->4成都");
        userMapper.updateUser("accatest","qcTest3","test4成都");
//
        try{
            int i=10/0;
        }catch (Exception e){
            log.info("异常信息：{}",e.getMessage());
        }

    }


}
