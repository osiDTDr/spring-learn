package com.bright.spring.learn.sale;

/**
 * 商品类
 *
 * @author zhengyuan
 * @since 2021/03/31
 */
public class GoodsVo {
    // 商品ID
    private long id;
    // 商品名称
    private String goodsName;
    // 商品标题
    private String goodsTitle;
    // 商品的图片
    private String goodsImg;
    // 商品详情
    private String goodsDetail;
    // 商品单价
    private double goodsPrice;
    // 商品库存  -1表示 没有限制
    private int goodsStock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }
}
