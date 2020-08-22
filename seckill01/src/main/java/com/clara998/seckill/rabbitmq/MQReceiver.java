package com.clara998.seckill.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.mapper.GoodsMapper;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.OrderService;
import com.clara998.seckill.service.SeckillService;
import com.clara998.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author clara
 * @date 2020/8/22
 */
@Component
@RabbitListener(queuesToDeclare = @Queue("queue"))
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RabbitHandler
    public void receive(String message) {
        log.info("receive message:"+message);
        SeckillMessage m = JSON.toJavaObject(JSON.parseObject(message), SeckillMessage.class);
        User user = m.getUser();
        long goodsId = m.getGoodsId();

        /**
         * 查看秒杀商品库存
         */
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            return;
        }

        /**
         * 判断重复秒杀
         */
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }

        /**
         * 减库存，下订单，写入秒杀订单
         */
        seckillService.seckill(user, goodsVo);
    }
}
