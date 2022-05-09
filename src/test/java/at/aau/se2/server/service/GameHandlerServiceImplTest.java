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
    public void createNewGameTest() {
        String gameId = service.createNewGame(player1);
        verify(gameRepository).add(any(Game.class));
        assertNotNull(gameId);
    }

    @Test
    public void joinExistingGameSuccessfullyTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        Integer returnedCode = service.joinGame(player1, game.getID());
        assertEquals(Game.JOINING_SUCCESSFUL, returnedCode);
    }

    @Test
    public void joinExistingGameFailTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        Player shouldNotBebAbleToJoin = new Player("player3");
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player1, game.getID()));
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player2, game.getID()));
        assertEquals(Game.GAME_FULL, service.joinGame(shouldNotBebAbleToJoin, game.getID()));
    }

    @Test
    public void joinNonExistingGameTest() {
        when(gameRepository.findById(game.getID())).thenReturn(null);
        assertEquals(Game.GAME_NOT_FOUND, service.joinGame(player1, game.getID()));
    }

    @Test
    public void endGame() {
        service.endGame("1");
        verify(gameRepository).remove("1");
    }

    @Test
    public void getOpponentCorrectTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName());
        PlayerDTO player2DTO = new PlayerDTO(player2.getName());
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getID());
        service.joinGame(player2, game.getID());
        assertEquals(player2DTO, playerMapper.map(service.getOpponentOf(player1, game.getID())));
        assertEquals(player1DTO, playerMapper.map(service.getOpponentOf(player2, game.getID())));
    }

    @Test
    public void getOpponentFailTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        service.joinGame(player1, game.getID());
        Player opponent = service.getOpponentOf(player1, game.getID());
        assertNotNull(opponent);
        assertNull(opponent.getName());
    }

    @Test
    public void setDiceValueGameNotFoundTest() {
        service.joinGame(player1, game.getID());
        service.joinGame(player2, game.getID());
        service.setDiceValueAndCompare(player1, game.getID(), 3);
        DiceResultDTO result = service.setDiceValueAndCompare(player2, game.getID(), 5);
        assertNull(result);
    }

    @Test
    public void setDiceValueTooFewValuesTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        service.joinGame(player1, game.getID());
        service.joinGame(player2, game.getID());
        DiceResultDTO result = service.setDiceValueAndCompare(player2, game.getID(), 3);
        assertNull(result);
    }

    @Test
    public void setDiceValueWinnerTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName());
        PlayerDTO player2DTO = new PlayerDTO(player2.getName());
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getID());
        service.joinGame(player2, game.getID());
        DiceResultDTO result = service.setDiceValueAndCompare(player1, game.getID(), 5);
        assertNull(result);
        result = service.setDiceValueAndCompare(player2, game.getID(), 3);
        assertNotNull(result);
        assertEquals(List.of(player1DTO, player2DTO), result.getPlayers());
        assertEquals(player1DTO, result.getWinner());
    }

    @Test
    public void setDiceValueNoWinnerTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        PlayerDTO player1DTO = new PlayerDTO(player1.getName(),3);
        PlayerDTO player2DTO = new PlayerDTO(player2.getName(), 3);
        when(playerMapper.map(player1)).thenReturn(player1DTO);
        when(playerMapper.map(player2)).thenReturn(player2DTO);
        service.joinGame(player1, game.getID());
        service.joinGame(player2, game.getID());
        DiceResultDTO result = service.setDiceValueAndCompare(player1, game.getID(), 3);
        assertNull(result);
        result = service.setDiceValueAndCompare(player2, game.getID(), 3);
        assertNotNull(result);
        assertNull(result.getWinner());
        assertEquals(List.of(player1DTO, player2DTO), result.getPlayers());
    }
}