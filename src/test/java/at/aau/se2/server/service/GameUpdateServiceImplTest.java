package at.aau.se2.server.service;

import at.aau.se2.server.dto.GameDataDTO;
import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameUpdateServiceImplTest {

    @InjectMocks
    private GameUpdateService service;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerMapper playerMapper;

    private Player p1, p2;
    private PlayerDTO player1DTO, player2DTO;

    private Game game;

    private ObjectMapper objectMapper;

    private GameDataDTO<String> gameDataDTO;

    @BeforeEach
    public void init() {
        service = new GameUpdateServiceImpl();
        MockitoAnnotations.openMocks(this);
        p1 = new Player("p1");
        p2 = new Player("p2");
        p1.setSessionId("1");
        p2.setSessionId("2");
        game = new Game();
        game.join(p1);
        game.join(p2);
        when(gameRepository.findById(game.getId())).thenReturn(game);
        objectMapper = new ObjectMapper();
        player1DTO = new PlayerDTO(p1.getName());
        player2DTO = new PlayerDTO(p2.getName());
        when(playerMapper.map(p1)).thenReturn(player1DTO);
        when(playerMapper.map(p2)).thenReturn(player2DTO);
        gameDataDTO = new GameDataDTO<>();
        gameDataDTO.setNextPlayer(player1DTO);
        gameDataDTO.setData("data");
    }

    @Test
    void gameUpdatePlayerNotAllowedToMakeMoveTest() throws JsonProcessingException {
        game.setPlayerOnTurn(p2);
        assertNull(service.onUpdate(gameDataDTO, p1, game.getId()));
    }

    @Test
    void gameUpdatePlayerAllowedToMakeMoveTest() throws JsonProcessingException {
        game.setPlayerOnTurn(p1);
        GameDataDTO<String> gameDataDTO = new GameDataDTO<>();
        gameDataDTO.setNextPlayer(player1DTO);
        gameDataDTO.setData("data");
        assertNotNull(service.onUpdate(gameDataDTO, p1, game.getId()));
    }

    @Test
    void switchPlayerOnTurnTest() throws JsonProcessingException {
        game.setPlayerOnTurn(p1);
        assertEquals(p1, game.getPlayerOnTurn());
        game.switchPlayerOnTurn();
        assertNull(service.onUpdate(gameDataDTO, p1, game.getId()));
    }

    @Test
    void gameFlowTest() throws JsonProcessingException {

        game.addDice(p1, 5);
        assertFalse(game.hasDiceRollWinner());
        game.addDice(p2, 5);
        assertFalse(game.hasDiceRollWinner());

        game.addDice(p1, 5);
        game.addDice(p2, 2);
        assertTrue(game.hasDiceRollWinner());
        assertEquals(p1, game.getDiceRollWinner());
        assertEquals(p1, game.getPlayerOnTurn());

        // it is p1's turn


        assertNull(service.onUpdate(gameDataDTO, p2, game.getId()));
        GameDataDTO<?> receivedData = service.onUpdate(gameDataDTO, p1, game.getId());
        assertEquals("data", objectMapper.convertValue(receivedData.getData(), String.class));

        // it is p2's turn now
        assertNull(service.onUpdate(gameDataDTO, p1, game.getId()));
        receivedData = service.onUpdate(gameDataDTO, p2, game.getId());
        assertEquals("data", objectMapper.convertValue(receivedData.getData(), String.class));
    }

    @Test
    void getGameStateTest() {
        game.setGameState(gameDataDTO);
        assertEquals(gameDataDTO, service.onSubscribed(p1, game.getId()));
    }

    @Test
    void getGameStateNoStateTest() {
        assertNull(service.onSubscribed(p1, game.getId()));
    }

}