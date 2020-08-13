package com.clara998.seckill.controller;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.result.CodeMsg;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.OrderService;
import com.clara998.seckill.service.SeckillService;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author clara
 * @date 2020/8/13
 */

@Controller
@RequestMapping("seckill")
public class SeckillController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("do_seckill")
    //疑惑这个goodId哪里来的
    public String list(Model model, User user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        //判断库存数量是否大于1
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "seckill_fail";
        }
        //通过用户id和商品id来判断该用户是否已经秒杀到该商品，防止重复秒杀
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_SECKILL);
            return "seckill_fail";
        }
        // 秒杀成功: 减库存、下订单、写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
