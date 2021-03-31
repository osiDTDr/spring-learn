package com.bright.spring.learn.sale;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public Order getOrderByUserIdGoodsId(long userId, long goodsId) {
        return new Order();
    }

    public OrderInfo createOrder(SaleUser saleUser, GoodsVo goodsVo) {
        return new OrderInfo();
    }
}
