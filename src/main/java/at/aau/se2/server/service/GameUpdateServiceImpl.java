package at.aau.se2.server.service;

import at.aau.se2.server.dto.GameDataDTO;
import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GameUpdateServiceImpl implements GameUpdateService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public GameDataDTO<?> onUpdate(GameDataDTO<?> payload, Player player, String gameId) {
        Game game = Objects.requireNonNull(gameRepository.findById(gameId));
        Player playerOnTurn = game.getPlayerOnTurn();

        if(!player.equals(playerOnTurn)) {
            return null;
        }

        game.switchPlayerOnTurn();
        payload.setNextPlayer(playerMapper.map(game.getPlayerOnTurn()));
        game.setGameState(payload);
        return payload;
    }

    @Override
    public GameDataDTO<?> onSubscribed(Player player, String gameId) {
        Game game = gameRepository.findById(gameId);
        player.setGameSubscribedTo(gameId);
        // If the game was already started send the last state to the subscribing player
        if(game.getGameState() == null) {
            return null;
        }
        return game.getGameState();
    }
}
