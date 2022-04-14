package at.aau.se2.server.repository;

public interface Repository<T, D> {
    D add(D value);
    void remove(T id);
    D findById(T id);
}
