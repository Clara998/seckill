package com.clara998.seckill.vo;

import com.clara998.seckill.bean.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author clara
 * @date 2020/8/21
 */

@Getter
@Setter
public class GoodsDetailVo {
    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private User user;
}
