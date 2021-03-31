package com.bright.spring.learn.sale;

/**
 * 秒杀请求消息结构体
 *
 * @author zhengyuan
 * @since 2021/03/31
 */
public class FlashSaleMessage {
    private SaleUser saleUser;
    private long goodsId;

    public FlashSaleMessage(SaleUser saleUser, long goodsId) {
        this.saleUser = saleUser;
        this.goodsId = goodsId;
    }

    public FlashSaleMessage() {
    }

    public SaleUser getSaleUser() {
        return saleUser;
    }

    public void setSaleUser(SaleUser saleUser) {
        this.saleUser = saleUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
