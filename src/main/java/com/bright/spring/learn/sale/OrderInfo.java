package com.bright.spring.learn.sale;

import java.util.Date;

/**
 * 订单信息类
 * @author zhengyuan
 * @since 2021/03/31
 */
public class OrderInfo {
    private long id;
    // 用户ID
    private long userId;
    // 商品id
    private long goodsId;
    // 收货地址ID
    private long deliveryAddrId;
    // 冗余过来的商品名称
    private String goodsName;
    // 商品数量
    private int goodsCount;
    // 商品单价
    private double goodsPrice;
    // 商品渠道, 1pc, 2android, 3ios
    private int orderChannel;
    // 订单状态， 0新建未支付， 1已支付， 2已发货， 3已收货， 4已退款， 5已完成
    private int status;
    // 订单的创建时间
    private Date createDate;
    // 支付时间
    private Date payDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getDeliveryAddrId() {
        return deliveryAddrId;
    }

    public void setDeliveryAddrId(long deliveryAddrId) {
        this.deliveryAddrId = deliveryAddrId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(int orderChannel) {
        this.orderChannel = orderChannel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
