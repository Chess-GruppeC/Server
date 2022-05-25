package at.aau.se2.server.service;

import at.aau.se2.server.entity.Player;

public interface GameUpdateService extends UpdateService<String, String> {
    String onSubscribed(String gameId, Player player);
}
