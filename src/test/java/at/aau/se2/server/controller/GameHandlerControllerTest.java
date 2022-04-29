package at.aau.se2.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerControllerTest extends AbstractControllerTest {

    @Test
    public void createGameTest() throws Exception {
        assertTrue(session.isConnected());
        session.subscribe("/user/queue/create", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.add((String) payload);
            }
        });

        session.send(getHeaders("/topic/create"), "");
        // Should receive an ID of length 5
        assertEquals(5, Objects.requireNonNull(blockingQueue.poll(1, TimeUnit.SECONDS)).length());
    }

    @Test
    public void joinGameFailTest() throws Exception {
        assertTrue(session.isConnected());

        session.subscribe("/user/queue/join", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.add((String) payload);
            }
        });

        session.send(getHeaders("/topic/join"), "anyId");
        // Should receive the response code "-1" which means that the game could not be found
        assertEquals("-1", blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    @Test
    public void joinGameSuccessfulTest() throws Exception {
        assertTrue(session.isConnected());

        session.subscribe("/user/queue/create", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                session.send(getHeaders("/topic/join"), payload);
            }
        });

        session.subscribe("/user/queue/join", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.add((String) payload);
            }
        });

        session.send(getHeaders("/topic/create"), "");

        // Should receive the response code "1" which means that the player joined successfully
        assertEquals("1", blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    private StompHeaders getHeaders(String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("simpUser", "Player");
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }
}