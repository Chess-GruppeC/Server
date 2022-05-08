package at.aau.se2.server.service;

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
        player2 = new Player("player2");
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
        assertEquals(player2DTO, service.getOpponent(player1, game.getID()));
        assertEquals(player1DTO, service.getOpponent(player2, game.getID()));
    }

    @Test
    public void getOpponentFailTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        service.joinGame(player1, game.getID());
        PlayerDTO opponent = service.getOpponent(player1, game.getID());
        assertNotNull(opponent);
        assertNull(opponent.getName());
    }

}