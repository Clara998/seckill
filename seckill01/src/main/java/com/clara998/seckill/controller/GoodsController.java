package com.clara998.seckill.controller;

import com.clara998.seckill.bean.User;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.UserService;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author clara
 * @date 2020/8/1
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        //往前台传数据
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

}
