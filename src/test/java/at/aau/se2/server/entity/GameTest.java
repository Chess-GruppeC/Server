package at.aau.se2.server.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game g1;
    private Player p1, p2, p3;

    @BeforeEach
    public void init() {
        g1 = new Game();
        p1 = new Player("player1");
        p1.setSessionId("1");
        p2 = new Player("player2");
        p2.setSessionId("2");
        p3 = new Player("player3");
        p3.setSessionId("3");
    }

    @Test
    public void generateRandomIdTest() {
        String randomId = g1.generateID(5);
        String regex = "[" + Arrays.toString(Game.ID_CHARACTERS) + "]{" + Game.ID_SIZE + "}";
        assertEquals(Game.ID_SIZE, randomId.length());
        assertTrue(randomId.matches(regex));
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

    @Test
    public void addDiceTooFewValuesTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        assertFalse(g1.hasDiceRollWinner());
    }

    @Test
    public void addDiceOnePlayerTwiceTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        g1.addDice(p1, 6);
        assertEquals(4, p1.getDiceValue()); // first set value should stay
    }

    @Test
    public void addDiceTest() {
        g1.join(p1);
        g1.join(p2);
        g1.addDice(p1, 4);
        assertEquals(4, p1.getDiceValue());
        assertFalse(g1.hasDiceRollWinner());
        g1.addDice(p2, 5);
        assertEquals(5, p2.getDiceValue());
        assertTrue(g1.hasDiceRollWinner());
        assertEquals(p2, g1.getDiceRollWinner());
    }

    @Test
    public void addDiceEqualValuesTest() {
        g1.join(p1);
        g1.join(p2);
        g1.addDice(p1, 4);
        g1.addDice(p2, 4);
        assertFalse(g1.hasDiceRollWinner());
    }

    @Test
    public void reRollAfterEqualDiceValuesTest() {
        g1.join(p1);
        g1.join(p2);
        g1.addDice(p1, 4);
        g1.addDice(p2, 4);
        assertFalse(g1.hasDiceRollWinner());
        g1.addDice(p1, 5);
        g1.addDice(p2, 4);
        assertTrue(g1.hasDiceRollWinner());
        assertEquals(p1, g1.getDiceRollWinner());
    }

    @Test
    public void resetDiceTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        assertNotNull(p1.getDiceValue());
        g1.resetDiceValues();
        assertNull(p1.getDiceValue());
    }

}