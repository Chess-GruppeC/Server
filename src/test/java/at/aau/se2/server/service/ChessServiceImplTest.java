package at.aau.se2.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceImplTest {

    private GameUpdateService service;

    @BeforeEach
    public void init() {
        service = new GameUpdateServiceImpl();
    }

    @Test
    void gameUpdateTest() {
        assertEquals("data", service.onUpdate("data", "id"));
    }

}