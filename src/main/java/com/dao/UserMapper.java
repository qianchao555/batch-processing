package com.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/19 21:18
 * @version:1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {



}
