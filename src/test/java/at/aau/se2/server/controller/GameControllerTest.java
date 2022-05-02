package at.aau.se2.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

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
                System.out.println("Received message: " + payload);
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
    public void getOpponentTest() throws InterruptedException {
        assertTrue(session.isConnected());

        session.subscribe("/user/queue/create", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                gameId = (String) payload;
                session.send(getHeaders("/topic/game/opponent"), gameId);
            }
        });

        session.subscribe("/user/queue/game/opponent", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        session.send(getHeaders("/topic/create"), "");
        // Should receive a response.
        assertNotNull(blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    private StompHeaders getHeaders(String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("simpUser", "Player");
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }
}