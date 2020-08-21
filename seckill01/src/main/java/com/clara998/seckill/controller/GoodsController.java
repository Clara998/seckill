package com.clara998.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.clara998.seckill.bean.User;
import com.clara998.seckill.result.Result;
import com.clara998.seckill.service.GoodsService;
import com.clara998.seckill.service.UserService;
import com.clara998.seckill.vo.GoodsDetailVo;
import com.clara998.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author clara
 * @date 2020/8/1
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 商品列表展示
     * QPS:496
     * 1000*10
     * produces:表示处理生产对象
    * */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {

        //取缓存
        String html = stringRedisTemplate.opsForValue().get("getGoodsList");
        if (!StringUtils.isEmpty(html)) {
            stringRedisTemplate.expire("getGoodsList",60, TimeUnit.SECONDS);
            return html;
        }

        //往前台传数据
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);

        //手动渲染
        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);

        if (!StringUtils.isEmpty(html)) {
            stringRedisTemplate.opsForValue().set("getGoodsList", html, 60, TimeUnit.SECONDS);
        }

        return html;
    }

    /**
     * 商品详情页,可是根本没有地方用到啊？
     * */
    @RequestMapping(value = "/to_detail2/{goodsId}", produces =  "text/html")
    @ResponseBody
    //@PathVariable("goodsId"))是url中的占位符号：
    // https://blog.csdn.net/yalishadaa/article/details/70555561
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = stringRedisTemplate.opsForValue().get("getGoodsDetail" + goodsId);
        if(!StringUtils.isEmpty(html)) {
            stringRedisTemplate.expire("getGoodsDetail",60, TimeUnit.SECONDS);
            return html;
        }

        //根据goodsId 查goods
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //getTime()返回毫秒
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        //秒杀还没开始，倒计时
        if (now < startTime) {
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) * 1000);

            //秒杀已经结束
        } else if (now > endTime) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        //手动渲染
        IWebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            //设置过期时间
            stringRedisTemplate.opsForValue().set("getGoodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;


    }

    /**
     * 商品详情页面,返回值是：Result<GoodsDetailVo>
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {

        //根据id查询商品详情
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;

        if (now < startTime) {//秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) {//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(seckillStatus);

        return Result.success(vo);
    }


}
