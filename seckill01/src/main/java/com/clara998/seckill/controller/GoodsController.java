package com.clara998.seckill.controller;

import com.clara998.seckill.bean.User;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.UserService;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 商品列表展示
    * */
    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        //往前台传数据
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    /**
     * 商品详情页
     * */
    @RequestMapping("/to_detail/{goodsId}")
    //@PathVariable("goodsId"))是url中的占位符号：
    // https://blog.csdn.net/yalishadaa/article/details/70555561
    public String detail(Model model, User user, @PathVariable("goodsId") long goodId) {
        model.addAttribute("user", user);

        //根据goodsId 查goods
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        model.addAttribute("goods", goods);

        //getTime()返回毫秒
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        //秒杀还没开始，倒计时
        if (now < startTime) {
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) * 1000);

            //秒杀已经结束
        } else if (now > endTime) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";


    }


}
