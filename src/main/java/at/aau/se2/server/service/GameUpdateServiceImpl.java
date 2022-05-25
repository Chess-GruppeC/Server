package at.aau.se2.server.service;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GameUpdateServiceImpl implements GameUpdateService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public String onUpdate(String payload, Player player, String gameId) {
        Game game = Objects.requireNonNull(gameRepository.findById(gameId));
        Player playerOnTurn = game.getPlayerOnTurn();

        if(!player.equals(playerOnTurn)) {
            return null;
        }

        game.setGameState(payload);
        game.switchPlayerOnTurn();
        return payload;
    }

    @Override
    public String onSubscribed(String gameId, Player player) {
        Game game = gameRepository.findById(gameId);
        player.setGameSubscribedTo(gameId);
        // If the game was already started send the last state to the subscribing player
        if(game.getGameState() == null) {
            return null;
        }
        return game.getGameState();
    }
}
