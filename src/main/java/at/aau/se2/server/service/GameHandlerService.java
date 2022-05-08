package at.aau.se2.server.service;

import at.aau.se2.server.entity.Player;
import at.aau.se2.server.dto.PlayerDTO;

public interface GameHandlerService {
    String createNewGame(Player creator);
    Integer joinGame(Player player, String gameId);
    void endGame(String gameId);
    PlayerDTO getOpponent(Player requestingPlayer, String gameId);
}
