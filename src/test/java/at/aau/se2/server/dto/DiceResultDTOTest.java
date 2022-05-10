package at.aau.se2.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiceResultDTOTest {

    @Test
    void jsonConversionTestShouldNotContainsKey() throws JsonProcessingException {
        PlayerDTO p1 = new PlayerDTO("player1");
        PlayerDTO p2 = new PlayerDTO("player2");
        DiceResultDTO result = new DiceResultDTO(List.of(p1, p2));
        String jsonString = new ObjectMapper().writeValueAsString(result);
        assertFalse(jsonString.contains("winner"));   // should not contain a key "winner"
        DiceResultDTO parsedObj = new ObjectMapper().readValue(jsonString, DiceResultDTO.class);
        assertNull(parsedObj.getWinner());
        assertEquals(2, parsedObj.getPlayers().size());
    }

    @Test
    void jsonConversionTestContainsKey() throws JsonProcessingException {
        PlayerDTO p1 = new PlayerDTO("player1");
        PlayerDTO p2 = new PlayerDTO("player2");
        DiceResultDTO result = new DiceResultDTO(List.of(p1, p2), p1);
        String jsonString = new ObjectMapper().writeValueAsString(result);
        assertTrue(jsonString.contains("winner"));   // should contain a key "winner"
        DiceResultDTO parsedObj = new ObjectMapper().readValue(jsonString, DiceResultDTO.class);
        assertNotNull(parsedObj.getWinner());
        assertEquals(2, parsedObj.getPlayers().size());
    }

}