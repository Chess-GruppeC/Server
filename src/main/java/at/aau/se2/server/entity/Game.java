package at.aau.se2.server.entity;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {

    private String ID;
    private final List<Player> players;

    public static final int PLAYERS_REQUIRED = 2;

    // constants
    public static final int GAME_NOT_FOUND = -1;
    public static final int GAME_FULL = 0;
    public static final int JOINING_SUCCESSFUL = 1;

    // 62 different characters to the power of 5 = 916 * 10^6 different IDs
    // Should be unique enough for our game
    private static final int ID_SIZE = 5;

    public Game() {
        players = new ArrayList<>();
        setRandomID();
    }

    public void setRandomID() {
        ID = generateID(ID_SIZE);
    }

    public String generateID(Integer size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    public String getID() {
        return ID;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean join(Player p) {
        Optional<String> usernames = players.stream().map(Player::getName)
                .filter(name -> name.equals(p.getName())).findFirst();
        if(usernames.isPresent()) {
            // player reconnected with new session id
            return true;
        }

        // check if the player can join
        if(players.size() < PLAYERS_REQUIRED) {
            players.add(p);
            return true;
        }
        return false;
    }

}
