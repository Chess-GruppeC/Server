package at.aau.se2.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiceResultDTO {

    private List<PlayerDTO> players;
    private PlayerDTO winner;

    public DiceResultDTO() {
        // Default constructor needed for Json conversion
    }

    public DiceResultDTO(List<PlayerDTO> players) {
        this.players = players;
    }

    public DiceResultDTO(List<PlayerDTO> players, PlayerDTO winner) {
        this.players = players;
        this.winner = winner;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public PlayerDTO getWinner() {
        return winner;
    }
}
