package com.clara998.seckill.service;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author clara
 * @date 2020/8/13
 */

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        goodsService.reduceStock(goods);
        //下订单 写入秒杀订单
        return orderService.createOrder(user, goods);
    }
}
