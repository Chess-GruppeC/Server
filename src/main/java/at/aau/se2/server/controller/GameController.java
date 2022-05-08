package at.aau.se2.server.controller;

import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.service.ChessService;
import at.aau.se2.server.service.GameHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private ChessService<String> chessService;

    @Autowired
    private GameHandlerService gameHandlerService;

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

    @MessageMapping("/game/opponent")
    @SendToUser(broadcast = false)
    public PlayerDTO getOpponent(@Payload String gameId, @Header("simpUser") Player requestingPlayer) {
        return gameHandlerService.getOpponent(requestingPlayer, gameId);
    }

}
