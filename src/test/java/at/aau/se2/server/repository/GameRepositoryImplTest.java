package at.aau.se2.server.repository;

import at.aau.se2.server.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryImplTest {

    private GameRepository gameRepository;
    private Game g1, g2;

    @BeforeEach
    public void init() {
        gameRepository = new GameRepositoryImpl();
        g1 = new Game();
        g2 = new Game();
    }

    @Test
    public void insertTest() {
        gameRepository.add(g1);
        assertEquals(1, gameRepository.size());
    }

    @Test
    public void insertGameWithDuplicateID() {
        gameRepository.add(g1);
        g2.setId(g1.getID());
        gameRepository.add(g2);
        assertNotEquals(g1.getID(), g2.getID());
        assertEquals(2, gameRepository.size());
    }

    @Test
    public void removeTest() {
        gameRepository.add(g1);
        gameRepository.remove(g1.getID());
        assertEquals(0, gameRepository.size());
    }

    @Test
    public void findByIdTest() {
        gameRepository.add(g1);
        assertEquals(g1, gameRepository.findById(g1.getID()));
    }

    @Test
    public void findAllTest() {
        gameRepository.add(g1);
        gameRepository.add(g2);
        assertEquals(Map.of(g1.getID(), g1, g2.getID(), g2), gameRepository.findAll());
    }

}