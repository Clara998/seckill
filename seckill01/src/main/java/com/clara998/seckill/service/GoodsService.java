package com.clara998.seckill.service;

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
}
