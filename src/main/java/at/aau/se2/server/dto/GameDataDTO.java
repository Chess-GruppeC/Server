package at.aau.se2.server.dto;

import java.util.List;

public class GameDataDTO<T> {

    private PlayerDTO nextPlayer;
    private T data;
    private List<Object> destroyedLocationsByAtomicMove;

    public GameDataDTO() {
        // Default constructor needed for Json conversion
    }

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

    public List<Object> getDestroyedLocationsByAtomicMove() {
        return destroyedLocationsByAtomicMove;
    }

    public void setDestroyedLocationsByAtomicMove(List<Object> destroyedLocationsByAtomicMove) {
        this.destroyedLocationsByAtomicMove = destroyedLocationsByAtomicMove;
    }
}
