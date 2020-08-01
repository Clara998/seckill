package com.clara998.seckill.service;

import com.clara998.seckill.bean.User;
import com.clara998.seckill.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author clara
 * @date 2020/7/27
 */

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getById(int id) {
        return userMapper.getById(id);
    }
}
