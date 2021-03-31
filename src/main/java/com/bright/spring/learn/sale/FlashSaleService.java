package com.bright.spring.learn.sale;

import com.bright.spring.learn.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class FlashSaleService {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    private static final char[] chars = {'+', '-', '*'};

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 将goodsId和verifyCode一同提交到服务端做校验，如果只提交goodsId，那么客户端仍然可以使用明文的方式获取随机生成的接口秒杀地址，
     * 但是，引入了verifyCode后，客户端需要将验证码也一起发送到服务端做验证，验证成功才返回随机生成的秒杀地址，不成功则返回非法请求
     *
     * @param saleUser   用户
     * @param goodsId    商品id
     * @param verifyCode 验证码
     * @return 是否是有效请求
     */
    public boolean checkVerifyCode(SaleUser saleUser, long goodsId, String verifyCode) {
        long id = saleUser.getId();
        return true;
    }

    public String createFlashSalePath(SaleUser saleUser, long goodsId) {
        String path = "";
        // 这里可以加到redis中
        redisManager.hashPut("path", "" + saleUser.getId() + "_" + goodsId, path);
        return path;
    }


    public BufferedImage createVerifyImg(SaleUser saleUser, long goodsId) {
        if (saleUser == null || goodsId <= 0) {
            return null;
        }
        // 生成验证码图片的代码
        int width = 80;//定义 图像宽高
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//生成一个内存中图像对象 ，宽高，类型
        Graphics g = image.getGraphics();//获取图像的graphics 对象，利用它就可以画图
        // set the background color
        g.setColor(new Color(0xDCDCDC));//设置背景颜色
        g.fillRect(0, 0, width, height);//背景颜色的填充
        // draw the border
        g.setColor(Color.black);//黑色的边框
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();//随机数
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);//在图片上生成 50个 干扰的点
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm); //生成我们的验证码
        g.setColor(new Color(0, 100, 0));//验证码的颜色
        g.setFont(new Font("Candara", Font.BOLD, 24));//字体
        g.drawString(verifyCode, 8, 24);//将 这个String 类型验证码 写在 图片上
        g.dispose();//关掉这个画笔
        //把验证码存到redis中
        int rnd = calc(verifyCode);//计算这个数学公式验证码的值
        //将这个计算的值放入 redis中等待 对比
        redisManager.hashPut("verify_code", saleUser.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }


    /**
     * 生成这个验证码
     *
     * @param rdm 随机数
     * @return 验证码
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);//生成三个随机数（10以内）
        char charOne = chars[rdm.nextInt(3)];//生成两个运算符// n 表示 10以内，不包括10的整数 0-9
        char charTwo = chars[rdm.nextInt(3)];
        //对其进行拼接，获得数学表达式
        return "" + num1 + charOne + num2 + charTwo + num3;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
            return (int) engine.eval(exp);
        } catch (Exception e) {
            logger.error("calc expression {} error ", exp, e);
            return 0;
        }
    }

    @Transactional
    public OrderInfo sale(SaleUser saleUser, GoodsVo goodsVo) {
        //减库存(减miaosha_goods的库存)， 写入秒杀订单
        boolean success = goodsService.reduceStock(goodsVo);
        if (success) {
            //下订单， 包含两步：一、order_info  二、miaosha_order  实际上是两步操作
            return orderService.createOrder(saleUser, goodsVo);
        } else {//商品已经被秒杀完了
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    public long getFlashResult(Long userId, long goodsId) {
        Order order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {//说明本次查询的时候，已经秒杀成功了
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1; //代表卖完了
            } else {
                return 0; //继续轮询
            }
        }

    }

    private void setGoodsOver(Long id) {
        redisManager.hashPut("goods_over", String.valueOf(id), true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisManager.hHasKey("goods_over", String.valueOf(goodsId));
    }
}
