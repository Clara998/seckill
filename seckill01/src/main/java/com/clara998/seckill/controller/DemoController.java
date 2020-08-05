package com.clara998.seckill.controller;


import com.clara998.seckill.bean.User;
import com.clara998.seckill.result.CodeMsg;
import com.clara998.seckill.result.Result;
import com.clara998.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * @author clara
 * @date 2020/7/27
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

//    @Autowired
//    private RedisTemplate<String, String> strRedisTemplate;
//    @Autowired
//    private RedisTemplate<String, Serializable> serializableRedisTemplate;
//
//    @Autowired
//    UserService userService;
//
//    @RequestMapping("/")
//    @ResponseBody
//    String home() {
//        return "Hello World";
//    }
//
//    @RequestMapping("/hello")
//    @ResponseBody
//    public Result<String> hello() {
//        return Result.success("hello, Clara");
//    }
//
//    @RequestMapping("/Error")
//    @ResponseBody
//    public Result<String> error() {
//        return Result.error(CodeMsg.SERVER_ERROR);
//    }
//
//    @RequestMapping("/Thymeleaf")
//    public String thymeleaf(Model model) { //mvc 中的 model（表，实体类） view(html) controller （json)
//        model.addAttribute("name", "clara");
//        return "hello";
//    }
//
//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public Result<Integer> redisSet() {
//        User user = new User();
//        user.setId(100);
//        user.setName("Clara");
//        serializableRedisTemplate.opsForValue().set("100", user);
//        return Result.success(1);
//    }
//
//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public Result<User> redisGet() {
//        User s= (User)serializableRedisTemplate.opsForValue().get("100");
//        return Result.success(s);
//    }
//
//    @RequestMapping("/db/doubleInsert")
//    @ResponseBody
//    public Result<Boolean> doubleInsert() {
//        try {
//            userService.doubleInsert();
//            return Result.success(true);
//        } catch (Exception e) {
//            return Result.error(CodeMsg.PRIMARY_ERROR);
//        }
//    }
//
//    @RequestMapping("/db/get")
//    @ResponseBody
//    public Result<User> dbGet() {
//        User user = userService.getById(1);
//        return Result.success(user);
//    }






}
