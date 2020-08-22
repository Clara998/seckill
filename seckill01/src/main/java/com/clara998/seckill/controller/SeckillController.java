package com.clara998.seckill.controller;

import com.clara998.seckill.bean.SeckillOrder;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.rabbitmq.MQSender;
import com.clara998.seckill.rabbitmq.SeckillMessage;
import com.clara998.seckill.result.CodeMsg;
import com.clara998.seckill.result.Result;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.OrderService;
import com.clara998.seckill.service.SeckillService;
import com.clara998.seckill.vo.GoodsVo;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author clara
 * @date 2020/8/13
 */

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisTemplate<String, Serializable> serializableRedisTemplate;

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    MQSender mqSender;

    //基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(10);

    //做标记，判断该商品是否没有库存(通过内存哈希，减少redis的压力
    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 秒杀页面静态化：原先return "login";一个html网页，现在只返回错误码
     * get:幂等，多次请求调用结果一样
     * POST：非幂等（这里为什么用POST
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "do_seckill", method = RequestMethod.POST)
    @ResponseBody
    //疑惑这个goodId哪里来的: html页面input进来
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId")long goodsId) {
        //不阻塞
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return  Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }
        //返回login.html
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);

        /**
         * 判断库存
         */
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //预减库存
        long stock = stringRedisTemplate.boundValueOps("getGoodsStock" + goodsId).increment(-1);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //通过用户id和商品id来判断该用户是否已经秒杀到该商品，防止重复秒杀
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        //入队：通过消息队列减少数据库的压力，并且使应用解耦合
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(message);
        return Result.success(0);//排队中,失败？
    }

    /**
     * 系统初始化：将将商品信息加载到redis和本地内存
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {
            stringRedisTemplate.opsForValue().set("getGoodsStock" + goods.getId(), String.valueOf(goods.getStockCount()));
            //初始化商品都是没有处理过的(不会出现库存<0
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }
}
