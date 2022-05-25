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

class GameUpdateServiceImplTest {

    @InjectMocks
    private GameUpdateService service;

    @Mock
    private GameRepository gameRepository;

    private Player p1, p2;

    private Game game;

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
    }

    @Test
    void gameUpdatePlayerNotAllowedToMakeMoveTest() {
        game.setPlayerOnTurn(p2);
        assertNull(service.onUpdate("data", p1, game.getId()));
    }

    @Test
    void gameUpdatePlayerAllowedToMakeMoveTest() {
        game.setPlayerOnTurn(p1);
        assertEquals("data", service.onUpdate("data", p1, game.getId()));
    }

    @Test
    void switchPlayerOnTurnTest() {
        game.setPlayerOnTurn(p1);
        assertEquals(p1, game.getPlayerOnTurn());
        game.switchPlayerOnTurn();
        assertNull(service.onUpdate("data", p1, game.getId()));
    }

    @Test
    void gameFlowTest() {
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
        assertNull(service.onUpdate("data", p2, game.getId()));
        assertEquals("data", service.onUpdate("data", p1,  game.getId()));

        // it is p2's turn now
        assertEquals(p2, game.getPlayerOnTurn());
        assertNull(service.onUpdate("data", p1, game.getId()));
        assertEquals("data", service.onUpdate("data", p2,  game.getId()));
    }

    @Test
    void getGameStateTest() {
        game.setGameState("state");
        assertEquals("state", service.onSubscribed(game.getId(), p1));
    }

    @Test
    void getGameStateNoStateTest() {
        assertNull(service.onSubscribed(game.getId(), p1));
    }

}