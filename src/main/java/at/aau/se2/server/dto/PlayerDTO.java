package at.aau.se2.server.dto;

public class PlayerDTO {

    private String name;
    private Integer diceValue;

    public PlayerDTO() {}

    public PlayerDTO(String name) {
        this.name = name;
    }

    public PlayerDTO(String name, Integer diceValue) {
        this.name = name;
        this.diceValue = diceValue;
    }

    public String getName() {
        return name;
    }

    public Integer getDiceValue() {
        return diceValue;
    }

}
