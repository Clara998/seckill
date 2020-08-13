package com.clara998.seckill.mapper;

import com.clara998.seckill.bean.OrderInfo;
import com.clara998.seckill.bean.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @author clara
 * @date 2020/8/13
 */

@Mapper
public interface OrderMapper {

    @Select("select * from sk_order where user_id = #{userId} and goods_id = #{goodsId}")
    public SeckillOrder getOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * 获取刚刚插入的ID：select last_insert_id()
     * @param orderInfo
     * @return
     */
    @Insert("insert into sk_order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into sk_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertSeckillOrder(SeckillOrder order);
}
