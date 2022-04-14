package at.aau.se2.server.service;

public interface ChessService<T> {
    T onUpdate(T payload, String gameKey);
}
