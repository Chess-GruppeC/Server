package at.aau.se2.server.service;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class GameHandlerServiceImpl implements GameHandlerService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public String createNewGame(Player creator) {
        Game game = new Game();
        game.join(creator);
        gameRepository.add(game);
        return game.getID();
    }

    @Override
    public Integer joinGame(Player player, String gameId) {
        Game existingGame = gameRepository.findById(gameId);
        if(existingGame != null) {
            return existingGame.join(player) ? Game.JOINING_SUCCESSFUL : Game.GAME_FULL;
        }
        return Game.GAME_NOT_FOUND;
    }

    @Override
    public void endGame(String gameId) {
        gameRepository.remove(gameId);
    }

    @Override
    public String getOpponent(Player requestingPlayer, String gameId) {
        Optional<Player> opponent = gameRepository.findById(gameId)
                .getPlayers()
                .stream()
                .filter(Predicate.not(player -> Objects.equals(player, requestingPlayer))).findFirst();
        if(opponent.isPresent()) {
            System.out.println(opponent.get().getName());
            return opponent.get().getName();
        }
        return "-1";
    }

}
