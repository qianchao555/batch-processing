package com.springbootproject.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springbootproject.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/30 19:58
 * @version:1.0
 */
@Repository
public interface UserDao extends BaseMapper<User> {
}
