package at.aau.se2.server.service;

import at.aau.se2.server.dto.DiceResultDTO;
import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameHandlerServiceImplTest {

    @InjectMocks
    private GameHandlerServiceImpl service;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerMapper playerMapper;

    private Player player1, player2;

    private Game game;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        player1 = new Player("player");
        player1.setSessionId("1");
        player2 = new Player("player2");
        player2.setSessionId("2");
        game = new Game();
    }

    @Test
    void createNewGameTest() {
        String gameId = service.createNewGame(player1);
        verify(gameRepository).add(any(Game.class));
        assertNotNull(gameId);
    }

    @Test
    void joinExistingGameSuccessfullyTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        Integer returnedCode = service.joinGame(player1, game.getId());
        assertEquals(Game.JOINING_SUCCESSFUL, returnedCode);
    }

    @Test
    void joinExistingGameFailTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        Player shouldNotBebAbleToJoin = new Player("player3");
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player1, game.getId()));
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player2, game.getId()));
        assertEquals(Game.GAME_FULL, service.joinGame(shouldNotBebAbleToJoin, game.getId()));
    }

    @Test
    void joinNonExistingGameTest() {
        when(gameRepository.findById(game.getId())).thenReturn(null);
        assertEquals(Game.GAME_NOT_FOUND, service.joinGame(player1, game.getId()));
    }

    @Test
    void endGame() {
        service.endGame("1");
        verify(gameRepository).remove("1");
    }

    @Test
    void getOpponentCorrectTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName());
        PlayerDTO player2DTO = new PlayerDTO(player2.getName());
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getId());
        service.joinGame(player2, game.getId());
        assertEquals(player2DTO, playerMapper.map(service.getOpponentOf(player1, game.getId())));
        assertEquals(player1DTO, playerMapper.map(service.getOpponentOf(player2, game.getId())));
    }

    @Test
    void getOpponentFailTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        service.joinGame(player1, game.getId());
        Player opponent = service.getOpponentOf(player1, game.getId());
        assertNotNull(opponent);
        assertNull(opponent.getName());
    }

    @Test
    void setDiceValueGameNotFoundTest() {
        service.joinGame(player1, game.getId());
        service.joinGame(player2, game.getId());
        service.setDiceValueAndCompare(player1, game.getId(), 3);
        DiceResultDTO result = service.setDiceValueAndCompare(player2, game.getId(), 5);
        assertNull(result);
    }

    @Test
    void setDiceValueTooFewValuesTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        service.joinGame(player1, game.getId());
        service.joinGame(player2, game.getId());
        DiceResultDTO result = service.setDiceValueAndCompare(player2, game.getId(), 3);
        assertNull(result);
    }

    @Test
    void setDiceValueWinnerTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName());
        PlayerDTO player2DTO = new PlayerDTO(player2.getName());
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getId());
        service.joinGame(player2, game.getId());
        DiceResultDTO result = service.setDiceValueAndCompare(player1, game.getId(), 5);
        assertNull(result);
        result = service.setDiceValueAndCompare(player2, game.getId(), 3);
        assertNotNull(result);
        assertEquals(List.of(player1DTO, player2DTO), result.getPlayers());
        assertEquals(player1DTO, result.getWinner());
    }

    @Test
    void setDiceValueNoWinnerTest() {
        when(gameRepository.findById(game.getId())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName(),3);
        PlayerDTO player2DTO = new PlayerDTO(player2.getName(), 3);
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getId());
        service.joinGame(player2, game.getId());
        DiceResultDTO result = service.setDiceValueAndCompare(player1, game.getId(), 3);
        assertNull(result);
        result = service.setDiceValueAndCompare(player2, game.getId(), 3);
        assertNotNull(result);
        assertNull(result.getWinner());
        assertEquals(List.of(player1DTO, player2DTO), result.getPlayers());
    }
}