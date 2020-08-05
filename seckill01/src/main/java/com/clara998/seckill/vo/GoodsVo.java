package com.clara998.seckill.vo;

import com.clara998.seckill.bean.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author clara
 * @date 2020/8/1
 */
@Getter
@Setter
public class GoodsVo extends Goods {
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
