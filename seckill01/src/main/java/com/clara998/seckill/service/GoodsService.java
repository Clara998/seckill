package com.clara998.seckill.service;

import com.clara998.seckill.bean.SeckillGoods;
import com.clara998.seckill.exception.GlobalException;
import com.clara998.seckill.mapper.GoodsMapper;
import com.clara998.seckill.result.CodeMsg;
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

    //乐观锁冲突最大重试次数
    private static final int DEFAULT_MAX_RETRIES = 5;

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
        int numAttempts = 0;
        int ret = 0;
        SeckillGoods sg = checkStock(goods.getId());
        do {
            numAttempts++;
            try {
                ret = goodsMapper.reduceStock(sg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ret != 0) {
                break;
            }
        } while (numAttempts < DEFAULT_MAX_RETRIES);

        return ret > 0;
    }

    private SeckillGoods checkStock(Long goodsId) {
        SeckillGoods sg = goodsMapper.checkStock(goodsId);
        if (sg.getStockCount() <= 0) {
            throw new GlobalException(CodeMsg.SECKILL_OVER);
        }
        return sg;
    }
}
