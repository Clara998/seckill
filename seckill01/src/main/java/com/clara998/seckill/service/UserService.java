package com.clara998.seckill.service;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.exception.GlobalException;
import com.clara998.seckill.mapper.UserMapper;
import com.clara998.seckill.result.CodeMsg;
import com.clara998.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author clara
 * @date 2020/7/27
 * service 业务层是最上层
 */

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;
    @Autowired
    private RedisTemplate<String, Serializable> serializableRedisTemplate;

    public User getById(long id) {
        return userMapper.getById(id);
    }


    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        if (!dbPass.equals(formPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        serializableRedisTemplate.opsForValue().set(token, user);
        Cookie cookie = new Cookie("token", token);
        // 以s为单位
        cookie.setMaxAge(3600*24*2);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
