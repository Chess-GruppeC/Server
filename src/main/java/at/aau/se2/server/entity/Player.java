package at.aau.se2.server.entity;

import java.security.Principal;

public class Player implements Principal {

    private String name;
    private String sessionId;

    public Player(String name) {
        this.name = name;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String getName() {
        return name;
    }

}
