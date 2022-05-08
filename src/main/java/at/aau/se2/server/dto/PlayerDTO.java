package at.aau.se2.server.dto;

public class PlayerDTO {

    private final String name;

    public PlayerDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
