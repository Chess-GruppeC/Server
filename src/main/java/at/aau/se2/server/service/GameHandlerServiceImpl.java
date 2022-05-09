package at.aau.se2.server.service;

import at.aau.se2.server.dto.DiceResultDTO;
import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GameHandlerServiceImpl implements GameHandlerService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerMapper playerMapper;

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
        if (existingGame != null) {
            return existingGame.join(player) ? Game.JOINING_SUCCESSFUL : Game.GAME_FULL;
        }
        return Game.GAME_NOT_FOUND;
    }

    @Override
    public void endGame(String gameId) {
        gameRepository.remove(gameId);
    }

    @Override
    public Player getOpponentOf(Player requestingPlayer, String gameId) {
        Game game = gameRepository.findById(gameId);
        if (game == null) {
            return new Player(null);
        }
        Optional<Player> opponent = game
                .getPlayers()
                .stream()
                .filter(Predicate.not(player -> Objects.equals(player, requestingPlayer))).findFirst();
        return opponent.orElseGet(() -> new Player(null));
    }

    @Override
    public DiceResultDTO setDiceValueAndCompare(Player player, String gameId, Integer diceValue) {
        Game game = gameRepository.findById(gameId);
        if (game != null) {
            game.addDice(player, diceValue);
            if(game.hasDiceRollWinner()) {
                Player winner = game.getDiceRollWinner();
                PlayerDTO winnerDTO  = playerMapper.map(winner);
                PlayerDTO otherPlayerDTO  = playerMapper.map(getOpponentOf(winner, gameId));
                System.out.println("Winner");
                return new DiceResultDTO(List.of(winnerDTO, otherPlayerDTO), winnerDTO);
            } else if(game.hasEqualDiceValues()) {
                List<PlayerDTO> playerDTOS = game.getPlayers()
                        .stream()
                        .map(p -> playerMapper.map(p)).collect(Collectors.toList());
                return new DiceResultDTO(playerDTOS);
            } else {
                return null;
            }
        }
        return null;
    }

}
