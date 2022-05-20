package at.aau.se2.server.service;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GameUpdateServiceImpl implements GameUpdateService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onUpdate(String payload, Player player, String gameId) {
        Game game = Objects.requireNonNull(gameRepository.findById(gameId));

        Player playerOnTurn = game.getPlayerOnTurn();
        if(!player.equals(playerOnTurn)) {
            return;
        }

        game.setPlayerOnTurn(game.getOpponentOf(player));
        simpMessagingTemplate.convertAndSend("/topic/update/" + gameId, payload);
    }
}
