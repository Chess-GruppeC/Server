package at.aau.se2.server.service;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
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

    private Player player;

    private Game game;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        player = new Player("player");
        game = new Game();
    }

    @Test
    public void createNewGameTest() {
        String gameId = service.createNewGame(player);
        verify(gameRepository).add(any(Game.class));
        assertNotNull(gameId);
    }

    @Test
    public void joinExistingGameSuccessfullyTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        Integer returnedCode = service.joinGame(player, game.getID());
        assertEquals(Game.JOINING_SUCCESSFUL, returnedCode);
    }

    @Test
    public void joinExistingGameFailTest() {
        when(gameRepository.findById(game.getID())).thenReturn(game);
        Player player2 = new Player("player2");
        Player shouldNotBebAbleToJoin = new Player("player3");
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player, game.getID()));
        assertEquals(Game.JOINING_SUCCESSFUL, service.joinGame(player2, game.getID()));
        assertEquals(Game.GAME_FULL, service.joinGame(shouldNotBebAbleToJoin, game.getID()));
    }

    @Test
    public void joinNonExistingGameTest() {
        when(gameRepository.findById(game.getID())).thenReturn(null);
        assertEquals(Game.GAME_NOT_FOUND, service.joinGame(player, game.getID()));
    }

    @Test
    public void endGame() {
        service.endGame("1");
        verify(gameRepository).remove("1");
    }

}