package at.aau.se2.server.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game g1;
    private Player p1, p2, p3;

    @BeforeEach
    public void init() {
        g1 = new Game();
        p1 = new Player("player1");
        p2 = new Player("player2");
        p3 = new Player("player3");
    }

    @Test
    public void generateRandomIdTest() {
        String randomId = g1.generateID(5);
        assertEquals(5, randomId.length());
    }

    @Test
    public void setRandomIdTest() {
        String temp = g1.getID();
        g1.setRandomID();
        assertNotEquals(temp, g1.getID());
    }

    @Test
    public void setIdTest() {
        g1.setId("1");
        assertEquals("1", g1.getID());
    }

    @Test
    public void getPlayersTest() {
        assertNotNull(g1.getPlayers());
    }

    @Test
    public void joinGameSuccessfulTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        assertEquals(2, g1.getPlayers().size());
    }

    @Test
    public void joinFullGameTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        assertFalse(g1.join(p3));
        assertEquals(2, g1.getPlayers().size());
    }

    @Test
    public void rejoinGameTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        assertTrue(g1.join(p1));
        assertEquals(2, g1.getPlayers().size());
    }

}