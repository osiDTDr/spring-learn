package com.bright.spring.learn.sale;

import com.bright.spring.learn.redis.RedisManager;
import com.bright.spring.learn.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 秒杀接口
 *
 * @author zhengyuan
 * @since 2021/03/31
 */
@RestController
@RequestMapping("/flash_sale")
public class FlashSaleController {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

    private final Map<Long, Boolean> isOverMap = new HashMap<>();

    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageSender messageSender;

    @PostMapping("/{path}/do_flash_sale")
    public String doFlashSale(@PathVariable String path, @RequestParam("goodsId") long goodsId, SaleUser saleUser) {
        // 判断内存标记
        boolean over = isOverMap.get(goodsId);
        if (over) {
            return "flash sale is over";
        }

        // 预减库存
        long stock = redisManager.hashDecr("goods_stock", String.valueOf(goodsId), 1);
        if (stock < 0) {
            isOverMap.put(goodsId, true);
            return "flash sale is over";
        }
        //判断是否已经进行了秒杀
        Order order = orderService.getOrderByUserIdGoodsId(saleUser.getId(), goodsId);
        if (order != null) {
            return "repeat flash request";
        }
        //入队
        FlashSaleMessage flashSaleMessage = new FlashSaleMessage();
        flashSaleMessage.setSaleUser(saleUser);
        flashSaleMessage.setGoodsId(goodsId);
        messageSender.sendMessage(flashSaleMessage);
        return "request success";
    }

    /**
     * 安全优化之 ---接口地址随机化(隐藏)
     * 1.点击秒杀之后，先访问该接口生成一个pathId，并存入redis 返回前端
     * 2.前端带着这个pathId去访问秒杀接口，如果传入的path和从redis取出的不一致，就认为 非法请求
     */
    @RequestMapping(value = "/getPath", method = RequestMethod.GET)
    public String getPath(SaleUser saleUser,
                          @RequestParam("goodsId") long goodsId,
                          @RequestParam(value = "verifyCode") String verifyCode) {
        if (saleUser == null) {
            return "session is empty";
        }
        boolean checkValid = flashSaleService.checkVerifyCode(saleUser, goodsId, verifyCode);
        if (!checkValid) {
            return "invalid request";
        }
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        // 随机生成一个 pathId 返回给前端
        String pathId = MD5Utils.MD5Encode(uuid, "UTF-8");

        redisManager.hashPut("path_id", StringUtils.EMPTY + saleUser.getId() + goodsId, pathId);

        return pathId;
    }

    /**
     * 秒杀验证码 接口
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)// 只允许POST 提交
    public String getVerifyCode(HttpServletResponse response, SaleUser saleUser,
                                @RequestParam("goodsId") long goodsId) {
        // 判断是否登陆
        if (saleUser == null) {
            return "session is empty";
        }
        BufferedImage image = flashSaleService.createVerifyImg(saleUser, goodsId);
        try {
            OutputStream out = response.getOutputStream();//用response的输出流输出这个图片
            ImageIO.write(image, "JPEG", out);//图片写入输出流
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "get verify code error";
        }
        return null;
    }

    /**
     * 后台轮询功能
     * orderId: 成功
     * -1   ： 秒杀失败
     * 0 ： 还在排队
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Long result(SaleUser saleUser, @RequestParam("goodsId") long goodsId) {
        //RequestParam可以把保单隐含提交的input标签对应name属性的元素取出
        if (saleUser == null) {
            return CodeMsg.SESSION_ERROR_CODE;
        }
        //根据result的三种数值范围，来判断下一步的操作
        return flashSaleService.getFlashResult(saleUser.getId(), goodsId);
    }
}
