package com.clara998.seckill.mapper;

import com.clara998.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author clara
 * @date 2020/8/1
 */

@Mapper
public interface GoodsMapper {
    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price  from sk_goods_seckill sg left join sk_goods g on sg.goods_id = g.id")
    public List<GoodsVo> goodsVoList();
}
