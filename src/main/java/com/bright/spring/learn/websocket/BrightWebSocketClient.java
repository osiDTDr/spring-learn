package com.bright.spring.learn.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * custom websocket client
 *
 * @author zhengyuan
 * @since 2020/11/24
 */
public class BrightWebSocketClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(BrightWebSocketClient.class);

    public BrightWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public BrightWebSocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    /**
     * Callback for string messages received from the remote host
     *
     * @param message The UTF-8 decoded message that was received.
     * @see #onMessage(ByteBuffer)
     **/
    @Override
    public void onMessage(String message) {
        logger.info("receive message from websocket server is {}", message);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
