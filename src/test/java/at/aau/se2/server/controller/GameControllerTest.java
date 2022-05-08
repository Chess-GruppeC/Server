package at.aau.se2.server.controller;

import at.aau.se2.server.dto.PlayerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest extends AbstractControllerTest {

    private String gameId;

    @Test
    public void verifyGameDataIsReceived() throws Exception {

        session.subscribe("/topic/update/1", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        // This subscription should not receive the game data as it has a wrong game ID
        session.subscribe("/topic/update/2", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                fail();
            }
        });

        session.send("/topic/game/1", "Data");
        assertEquals("Data", blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    @Test
    public void getOpponentTest() throws InterruptedException, ExecutionException, TimeoutException {
        BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);

        WebSocketStompClient webSocketStompClientJSON = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        webSocketStompClientJSON.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession sessionJSON = webSocketStompClientJSON
                .connect(String.format(getWsPath(), port), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        assertTrue(sessionJSON.isConnected());

        sessionJSON.subscribe("/user/queue/game/opponent", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                queue.add(payload);
            }

        });

        sessionJSON.send(getHeaders("/topic/game/opponent"), "1");
        // Should receive a response.
        assertNotNull(queue.poll(1, TimeUnit.SECONDS));
    }

    private StompHeaders getHeaders(String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("simpUser", "Player");
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }
}