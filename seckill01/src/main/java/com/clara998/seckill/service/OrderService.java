package com.clara998.seckill.service;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.SeckillGoods;
import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.mapper.OrderMapper;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author clara
 * @date 2020/8/13
 */

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return orderMapper.getOrderByUserIdGoodsId(userId, goodsId);
    }

    /**
     * 因为要同时分别在订单详情表和秒杀订单表都新增一条数据，所以要保证两个操作是一个事物
     */
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        //返回的主键id
        long orderId = orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }

}
