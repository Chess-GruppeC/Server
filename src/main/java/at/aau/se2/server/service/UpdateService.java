package at.aau.se2.server.service;

import at.aau.se2.server.entity.Player;

public interface UpdateService<D> {
    void onUpdate(D payload, Player player, String gameId);
}
