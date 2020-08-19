package com.clara998.seckill.controller;

import com.clara998.seckill.bean.User;
import com.clara998.seckill.result.Result;
import com.clara998.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author clara
 * @date 2020/8/19
 */

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model, User user) {

        return Result.success(user);
    }

}
