package at.aau.se2.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessServiceImplTest {

    private ChessService<String> service;

    @BeforeEach
    public void init() {
        service = new ChessServiceImpl();
    }

    @Test
    void gameUpdateTest() {
        assertEquals("data", service.onUpdate("data", "id"));
    }

}