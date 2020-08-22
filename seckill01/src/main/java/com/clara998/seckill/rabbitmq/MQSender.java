package com.clara998.seckill.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.clara998.seckill.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author clara
 * @date 2020/8/22
 */

@Service
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static Logger log = LoggerFactory.getLogger(MQSender.class);


    //hello world
    public void sendSeckillMessage(SeckillMessage message){
        String msg = JSON.toJSONString(message);
        log.info("send message:"+msg);
        rabbitTemplate.convertAndSend("queue", msg);
    }
}
