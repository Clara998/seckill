package com.clara998.seckill.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Date;

/**
 * @author clara
 * @date 2020/8/7
 */
@Getter
@Setter
public class SeckillGoods implements Serializable {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private int version;
}
