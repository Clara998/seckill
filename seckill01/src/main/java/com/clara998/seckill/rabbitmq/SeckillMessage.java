package com.clara998.seckill.rabbitmq;

import com.clara998.seckill.bean.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/22
 */

@Getter
@Setter
public class SeckillMessage {
    private User user;
    private long goodsId;
}
