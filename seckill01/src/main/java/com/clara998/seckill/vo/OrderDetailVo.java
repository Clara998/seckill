package com.clara998.seckill.vo;

import com.clara998.seckill.bean.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/21
 */

@Getter
@Setter
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
