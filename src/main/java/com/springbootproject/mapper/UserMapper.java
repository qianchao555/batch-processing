package com.springbootproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:18
 * @version:1.0
 */
@Mapper
@Repository
public interface UserMapper {

    int insertUser(String id);

}
