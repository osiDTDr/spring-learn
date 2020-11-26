package com.bright.spring.learn.websocket;

import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * @author zhengyuan
 * @since 2020/11/24
 */
public class WebSocketTest {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketTest.class);

    /**
     * Test steps
     * 1. start up SpringLearnApplication
     * 2. test this method
     */
    @Test
    public void testWebSocket() {
        try {
            BrightWebSocketClient webSocketClient = new BrightWebSocketClient("ws://localhost:8080/websocket");
            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                logger.info("web socket not connect");
            }
            logger.info("websocket already connected");
            webSocketClient.send("test message");
            // sleep 10S, avoid client shutdown, waiting for websocket response message
            Thread.sleep(10_1000);
        } catch (URISyntaxException e) {
            logger.error("convert to uri error ", e);
        } catch (InterruptedException e) {
            logger.error("thread Interrupted error ", e);
            Thread.currentThread().interrupt();
        }
    }
}
