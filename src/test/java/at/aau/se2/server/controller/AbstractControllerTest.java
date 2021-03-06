package at.aau.se2.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractControllerTest {

    @LocalServerPort
    protected Integer port;

    protected StompSession session;

    protected BlockingQueue<String> blockingQueue;

    @BeforeEach
    public void setup() throws Exception {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        session = webSocketStompClient
                .connect(String.format(getWsPath(), port), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        blockingQueue = new ArrayBlockingQueue<>(1);
    }

    @Test
    void isSessionConnectedTest() {
        assertNotNull(session);
        assertTrue(session.isConnected());
    }

    protected String getWsPath() {
        return String.format("ws://localhost:%d/chess", port);
    }

}
