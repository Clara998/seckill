package com.clara998.seckill.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author clara
 * @date 2020/8/13
 * 订单信息
 */
@Setter
@Getter
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
