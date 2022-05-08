package at.aau.se2.server.mapper;

public interface Mapper<T, D> {
    D map(T t);
}
