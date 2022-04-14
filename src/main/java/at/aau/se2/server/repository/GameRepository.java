package at.aau.se2.server.repository;

import at.aau.se2.server.entity.Game;

import java.util.Map;

public interface GameRepository extends Repository<String, Game> {
    int size();
    Map<String, Game> findAll();
}
