package at.aau.se2.server.service;

import at.aau.se2.server.entity.Player;

public interface UpdateService<T, D> {
    T onUpdate(D payload, Player player, String gameId);
}
