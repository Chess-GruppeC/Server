package at.aau.se2.server.service;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChessServiceImplTest {

    @InjectMocks
    private GameUpdateService service;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private Player p1, p2;

    private Game game;

    @BeforeEach
    public void init() {
        service = new GameUpdateServiceImpl();
        MockitoAnnotations.openMocks(this);
        p1 = new Player("p1");
        p2 = new Player("p2");
        game = new Game();
        game.join(p1);
        game.join(p2);
        when(gameRepository.findById(game.getId())).thenReturn(game);
    }

    @Test
    void gameUpdatePlayerNotAllowedToMakeMoveTest() {
        game.setPlayerOnTurn(p2);
        service.onUpdate("data", p1, game.getId());
        verifyNoInteractions(simpMessagingTemplate);
    }

    @Test
    void gameUpdatePlayerAllowedToMakeMoveTest() {
        game.setPlayerOnTurn(p1);
        service.onUpdate("data", p1, game.getId());
        verify(simpMessagingTemplate, times(1)).convertAndSend("/topic/update/" + game.getId(), "data");
    }

    @Test
    void switchPlayerOnTurnTest() {
        game.setPlayerOnTurn(p1);
        assertEquals(p1, game.getPlayerOnTurn());
        game.switchPlayerOnTurn();
        service.onUpdate("data", p1, game.getId());
        verifyNoInteractions(simpMessagingTemplate);
    }

}