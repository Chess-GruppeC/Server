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
    void generateRandomIdTest() {
        String randomId = g1.generateID(5);
        String regex = "[" + Arrays.toString(Game.ID_CHARACTERS) + "]{" + Game.ID_SIZE + "}";
        assertEquals(Game.ID_SIZE, randomId.length());
        assertTrue(randomId.matches(regex));
    }

    @Test
    void setRandomIdTest() {
        String temp = g1.getId();
        g1.setRandomID();
        assertNotEquals(temp, g1.getId());
    }

    @Test
    void setIdTest() {
        g1.setId("1");
        assertEquals("1", g1.getId());
    }

    @Test
    void getPlayersTest() {
        assertNotNull(g1.getPlayers());
    }

    @Test
    void joinGameSuccessfulTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        assertEquals(2, g1.getPlayers().size());
    }

    @Test
    void joinFullGameTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        assertFalse(g1.join(p3));
        assertEquals(2, g1.getPlayers().size());
    }

    @Test
    void rejoinGameTest() {
        assertTrue(g1.join(p1));
        assertTrue(g1.join(p2));
        p1.setSessionId("newSession");
        assertTrue(g1.join(p1));
        assertEquals(2, g1.getPlayers().size());
    }

    @Test
    void getOpponentCorrectTest() {
        g1.join(p1);
        g1.join(p2);
        Player opponent = g1.getOpponentOf(p1);
        assertEquals(p2, opponent);
    }

    @Test
    void getOpponentFailTest() {
        g1.join(p1);
        Player opponent = g1.getOpponentOf(p1);
        assertNotNull(opponent);
        assertNull(opponent.getName());
    }

    @Test
    void addDiceTooFewValuesTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        assertFalse(g1.hasDiceRollWinner());
    }

    @Test
    void addDiceOnePlayerTwiceTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        g1.addDice(p1, 6);
        assertEquals(4, p1.getDiceValue()); // first set value should stay
    }

    @Test
    void addDiceTest() {
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
    void addDiceEqualValuesTest() {
        g1.join(p1);
        g1.join(p2);
        g1.addDice(p1, 4);
        g1.addDice(p2, 4);
        assertFalse(g1.hasDiceRollWinner());
    }

    @Test
    void reRollAfterEqualDiceValuesTest() {
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
    void resetDiceTest() {
        g1.join(p1);
        g1.addDice(p1, 4);
        assertNotNull(p1.getDiceValue());
        g1.resetDiceValues();
        assertNull(p1.getDiceValue());
    }

}