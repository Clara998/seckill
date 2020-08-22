package com.clara998.seckill.service;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    RedisTemplate<String, String> stringStringRedisTemplate;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods) {
        //减库存
        boolean success = goodsService.reduceStock(goods);
        /**
         * 查看是否库存<0,不能成功创建新订单
         */
        if (success){
            //下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 查看是否存在一个用户重复秒杀的情况
     * @param userId
     * @param goodsId
     * @return
     */
    public long getSeckillResult(long userId, long goodsId){
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){
            return order.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    /**
     * 商品没有库存则在redis中存isgoodsover+goodsdid。
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId) {
        stringStringRedisTemplate.opsForValue().set("isGoodsOver" + goodsId, "1");
    }

    private boolean getGoodsOver(long goodsId) {
        return stringStringRedisTemplate.hasKey("isGoodsOver" + goodsId);

    }
}
