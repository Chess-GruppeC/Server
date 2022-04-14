package at.aau.se2.server.service;

import org.springframework.stereotype.Service;

@Service
public class ChessServiceImpl implements ChessService<String> {

    @Override
    public String onUpdate(String payload, String gameKey) {
        // work with the incoming data...
        return "GAME UPDATE"; // send updated data to all players
    }
}
