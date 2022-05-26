package at.aau.se2.server.dto;

public class GameDataDTO<T> {

    private PlayerDTO nextPlayer;
    private T data;

    public GameDataDTO() {}

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PlayerDTO getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(PlayerDTO nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
}
