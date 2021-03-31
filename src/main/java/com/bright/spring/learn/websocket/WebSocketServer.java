package com.bright.spring.learn.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengyuan
 * @since 2020/11/23
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // 记录当前在线连接数
    private static final AtomicInteger onLineCount = new AtomicInteger(0);

    // 存放每个客户端对应的MyWebSocket对象。
    private static final ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        // 加入map中
        webSocketMap.put(session.getId(), this);
        // 在线数加1
        addOnlineCount();
        logger.info("new connection added, current connection number is {}", getOnlineCount());
        try {
            sendMessage("websocket response massage for open event");
        } catch (IOException e) {
            logger.error("io error when websocket server open ", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 从map中删除
        webSocketMap.remove(session.getId());
        // 连接数减1
        subOnlineCount();
        logger.info("Connection closed, current connection number is {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("receive message from websocket client is {}", message);

        // 群发消息
        for (WebSocketServer item : webSocketMap.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error("websocket server send message io error ", e);
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session 客户端连接会话
     * @param error   {@link Throwable} 错误异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("{} connection error ", session.getId(), error);
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
//        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (WebSocketServer item : webSocketMap.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                logger.error("send message error ", e);
            }
        }
    }

    public static int getOnlineCount() {
        return onLineCount.get();
    }

    public static void addOnlineCount() {
        onLineCount.incrementAndGet();
    }

    public static void subOnlineCount() {
        onLineCount.decrementAndGet();
    }
}
