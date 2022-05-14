package at.aau.se2.server.mapper;

import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperImplTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    public void init() {
        playerMapper = new PlayerMapperImpl();
    }

    @Test
    void mappingTest() {
        Player player = new Player("Player");
        player.setDiceValue(4);
        PlayerDTO playerDTO = playerMapper.map(player);
        assertEquals(player.getName(), playerDTO.getName());
        assertEquals(player.getDiceValue(), playerDTO.getDiceValue());
    }
}