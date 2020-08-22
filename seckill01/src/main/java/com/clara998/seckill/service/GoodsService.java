package com.clara998.seckill.service;

import com.clara998.seckill.bean.SeckillGoods;
import com.clara998.seckill.mapper.GoodsMapper;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author clara
 * @date 2020/8/1
 */

@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<GoodsVo> goodsVoList() {

        return goodsMapper.goodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {

        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 库存每次减少1
     * @return
     */
    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods sg = new SeckillGoods();
        sg.setGoodsId(goods.getId());
        //这里假如满足库存>0才update成功，否则update失败
        int ret = goodsMapper.reduceStock(sg);
        return ret > 0;
    }
}
