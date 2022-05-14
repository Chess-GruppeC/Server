package at.aau.se2.server.service;

import at.aau.se2.server.dto.DiceResultDTO;
import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Player;

public interface GameHandlerService {
    String createNewGame(Player creator);
    Integer joinGame(Player player, String gameId);
    void endGame(String gameId);
    PlayerDTO getOpponentOf(Player requestingPlayer, String gameId);
    DiceResultDTO setDiceValueAndCompare(Player player, String gameId, Integer diceValue);
}
