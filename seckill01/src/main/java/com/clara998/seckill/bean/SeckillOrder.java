package com.clara998.seckill.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/13
 * 秒杀订单
 */


@Getter
@Setter
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
