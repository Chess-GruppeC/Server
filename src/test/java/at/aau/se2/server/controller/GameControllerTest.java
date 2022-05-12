package at.aau.se2.server.controller;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GameRepository repository;

    @Autowired
    private GameController gameController;

    private Game game;
    private Player player;

    @BeforeEach
    public void init() {
        game = new Game();
        player = new Player("player");
        player.setSessionId("1");
        game.join(player);
        repository.add(game);
    }

    @Test
    void verifyGameDataIsReceived() throws Exception {

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
    void getOpponentTest() throws InterruptedException, ExecutionException, TimeoutException {
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

    @Test()
    @Disabled("Lacks proper test implementation")
    void sendDiceValueTest() throws InterruptedException, ExecutionException, TimeoutException {

        BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);

        WebSocketStompClient webSocketStompClientJSON = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        webSocketStompClientJSON.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession sessionJSON = webSocketStompClientJSON
                .connect(String.format(getWsPath(), port), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        assertTrue(sessionJSON.isConnected());

        sessionJSON.subscribe("/topic/getStartingPlayer/" + game.getId(), new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                queue.add(payload);
            }

        });

        sessionJSON.send(getHeaders("/topic/game/rollDice/" + game.getId()), "1");
    }

    private StompHeaders getHeaders(String destination) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setSession(player.getSessionId());
        stompHeaders.add("simpUser", player.getName());
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }
}