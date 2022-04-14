package at.aau.se2.server.controller;

import at.aau.se2.server.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private ChessService<String> chessService;

    /**
     * Passes the incoming data to the {@link ChessService#onUpdate(Object, String)} method to handle and broadcasts the returned value to all players
     * subscribed to the destination game ID
     * @param gameId
     * @param payload The game data
     * @return The updated game data to broadcast to the players
     */
    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/update/{gameId}")
    public String gameUpdate(@DestinationVariable String gameId, @Payload String payload) {
        return chessService.onUpdate(payload, gameId);
    }

}
