package at.aau.se2.server.controller;

import at.aau.se2.server.dto.GameDataDTO;
import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import at.aau.se2.server.repository.GameRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest extends AbstractControllerTest {

    private static final CountDownLatch waiter = new CountDownLatch(1);

    @Autowired
    private GameRepository repository;

    @SpyBean
    private GameRepository repositorySpy;

    private Game game;
    private Player player, player2;

    private GameDataDTO<String> gameDataDTO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        player = new Player("player");
        player2 = new Player("player2");
        game.join(player);
        game.join(player2);
        game.setPlayerOnTurn(player);
        repository.add(game);
        gameDataDTO = new GameDataDTO<>();
        gameDataDTO.setData("data");
    }

    @Test
    void verifyGameUpdateControllerIsCalled() throws Exception {
        // Re initialize the session because a MappingJackson2MessageConverter is needed
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        session = webSocketStompClient
                .connect(String.format(getWsPath(), port), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.send(getHeaders("/topic/game/" + game.getId()), gameDataDTO);
        waiter.await(1, TimeUnit.SECONDS);  // wait one second

        // Test if the findById() method from the GameRepository is called with the correct game ID
        // That implicates that the controller method is called correctly
        verify(repositorySpy).findById(game.getId());
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