package at.aau.se2.server.entity;

import java.security.Principal;

public class Player implements Principal {

    private final String name;
    private Integer diceValue;
    private String sessionId;
    private String gameSubscribedTo;

    public Player(String name) {
        this.name = name;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setDiceValue(Integer value) {
        if(diceValue == null) {
            diceValue = value;
        }
    }

    public void resetDiceValue() {
        diceValue = null;
    }

    public Integer getDiceValue() {
        return diceValue;
    }

    public String getGameSubscribedTo() {
        return gameSubscribedTo == null ? "" : gameSubscribedTo.replace("/topic/update/", "");
    }

    public void setGameSubscribedTo(String gameSubscribedTo) {
        this.gameSubscribedTo = gameSubscribedTo;
    }

    @Override
    public String getName() {
        return name;
    }

}
