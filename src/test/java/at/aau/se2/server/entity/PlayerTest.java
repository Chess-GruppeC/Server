package at.aau.se2.server.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player p1;

    @BeforeEach
    public void init() {
        p1 = new Player("player1");
        assertEquals("player1", p1.getName());
    }

    @Test
    public void setSessionId() {
        p1.setSessionId("1");
        assertEquals("1", p1.getSessionId());
    }

}