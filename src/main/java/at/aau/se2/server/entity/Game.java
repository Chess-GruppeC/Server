package at.aau.se2.server.entity;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.function.Predicate;

public class Game {

    private String id;
    private final List<Player> players;
    private Player diceRollWinner;
    private Player playerOnTurn;

    public static final int PLAYERS_REQUIRED = 2;

    // status codes
    public static final int GAME_NOT_FOUND = -1;
    public static final int GAME_FULL = 0;
    public static final int JOINING_SUCCESSFUL = 1;

    // 58 different characters to the power of 5 = 656 * 10^6 different IDs
    // Should be unique enough for our game
    public static final int ID_SIZE = 5;

    // Base58 characters for human readability
    protected static final char[] ID_CHARACTERS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();

    private boolean hasEqualDiceValues = false;

    public Game() {
        players = new ArrayList<>();
        setRandomID();
    }

    public void setRandomID() {
        id = generateID(ID_SIZE);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String generateID(Integer size) {
        return RandomStringUtils.random(size, ID_CHARACTERS);
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean join(Player p) {
        Optional<Player> player = players.stream()
                .filter(pl -> pl.getName().equals(p.getName())).findFirst();
        if (player.isPresent()) {
            // player reconnected with new session id
            players.remove(player.get());
            players.add(p);
            return true;
        }

        // check if the player can join
        if (players.size() < PLAYERS_REQUIRED) {
            players.add(p);
            return true;
        }

        return false;
    }

    public Player getOpponentOf(Player requestingPlayer) {
        Optional<Player> opponent = players
                .stream()
                .filter(Predicate.not(player -> Objects.equals(player, requestingPlayer))).findFirst();
        return opponent.orElseGet(() -> new Player(null));
    }

    public void addDice(Player player, Integer value) {
        players.stream()
                .filter(p -> p.getName().equals(player.getName()) && p.getSessionId().equals(player.getSessionId()))
                .findFirst()
                .ifPresent(p -> p.setDiceValue(value));

        int amountOfDiceValues = (int) players.stream().map(Player::getDiceValue)
                .filter(Objects::nonNull).count();

        if (amountOfDiceValues == PLAYERS_REQUIRED) {
            if (allDiceValuesEqual()) {
                resetDiceValues();
            } else {
                players.stream()
                        .max(Comparator.comparing(Player::getDiceValue))
                        .ifPresent(p -> diceRollWinner = playerOnTurn = p);
                hasEqualDiceValues = false;
            }
        } else {
            hasEqualDiceValues = false;
        }
    }

    public void resetDiceValues() {
        players.forEach(Player::resetDiceValue);
        diceRollWinner = null;
        hasEqualDiceValues = true;
    }

    private boolean allDiceValuesEqual() {
        return players.stream()
                .map(Player::getDiceValue)
                .distinct().count() == 1;
    }

    public boolean hasEqualDiceValues() {
        return hasEqualDiceValues;
    }

    public boolean hasDiceRollWinner() {
        return diceRollWinner != null;
    }

    public Player getDiceRollWinner() {
        return diceRollWinner;
    }

    public Player getPlayerOnTurn() {
        return playerOnTurn;
    }

    public void setPlayerOnTurn(Player newPlayerOnTurn) {
        this.playerOnTurn = newPlayerOnTurn;
    }

    public void switchPlayerOnTurn() {
        this.playerOnTurn = getOpponentOf(playerOnTurn);
    }
}
