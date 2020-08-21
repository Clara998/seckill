package com.clara998.seckill.service;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.SeckillGoods;
import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.mapper.OrderMapper;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

/**
 * 新订单创建时：(有什么用？？）
 *  同时分别在订单详情表和秒杀订单表都新增一条数据，将userid 和 goodsid 作为key 在 redis 中存seckillOrder
 * getOrder时:
 *  从 redis 取数据，而不是orderMapper
 *
 * @author clara
 * @date 2020/8/13
 */

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisTemplate<String, Serializable> serializableRedisTemplate;

    //在缓存中取秒杀订单
    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return (SeckillOrder) serializableRedisTemplate.opsForValue().get("seckill" + userId + "_" + goodsId);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
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

        /**
         * 在数据库中设置联合唯一索引，防止一个用户多次秒杀同一个商品
         */
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);
        serializableRedisTemplate.opsForValue().set("seckill" + user.getId() + "_" + goods.getId(), (Serializable)seckillOrder);
        return orderInfo;
    }

}
