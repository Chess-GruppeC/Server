package at.aau.se2.server.repository;

import at.aau.se2.server.entity.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Repository using a HashMap
 */
@Component
public class GameRepositoryImpl implements GameRepository {

    private final Map<String, Game> games = new HashMap<>();

    @Override
    public Game findById(String key) {
        return games.get(key);
    }

    @Override
    public Game add(Game g) {
        if (games.containsKey(g.getID())) {
            // Shouldn't happen
            g.setRandomID();  // try another random ID
        }
        return games.put(g.getID(), g);
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
