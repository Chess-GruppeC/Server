package at.aau.se2.server.service;

public interface UpdateService<T, D> {
    T onUpdate(D payload, String gameKey);

}
