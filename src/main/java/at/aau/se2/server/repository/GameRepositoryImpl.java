package at.aau.se2.server.repository;

import at.aau.se2.server.entity.Game;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Repository using a ConcurrentHashMap
 */
@Component
public class GameRepositoryImpl implements GameRepository {

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    @Override
    public Game findById(String key) {
        return games.get(key);
    }

    @Override
    public Game add(Game g) {
        if (games.containsKey(g.getId())) {
            // Shouldn't happen
            g.setRandomID();  // try another random ID
        }
        return games.putIfAbsent(g.getId(), g);
    }

    @Override
    public void remove(String key) {
       games.remove(key);
    }

    @Override
    public int size() {
        return games.size();
    }

    @Override
    public Map<String, Game> findAll() {
        return games;
    }
}
