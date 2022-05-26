package at.aau.se2.server.service;

import at.aau.se2.server.entity.Player;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UpdateService<T, D> {
    T onUpdate(D payload, Player player, String gameId) throws JsonProcessingException;
    T onSubscribed(Player player, String gameId);
}
