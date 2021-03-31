package com.bright.spring.learn.sale;

import org.springframework.stereotype.Component;

/**
 * 发送消息到MQ
 */
@Component
public class MessageSender {
    public void sendMessage(FlashSaleMessage flashSaleMessage) {
        // send to mq
    }
}
