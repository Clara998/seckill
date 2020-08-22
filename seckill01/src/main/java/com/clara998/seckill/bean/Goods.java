package com.clara998.seckill.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author clara
 * @date 2020/8/1
 */
@Setter
@Getter
public class Goods implements Serializable {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
