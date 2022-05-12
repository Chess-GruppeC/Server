package at.aau.se2.server.controller;

import at.aau.se2.server.entity.Player;
import at.aau.se2.server.service.GameHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameHandlerController {

    @Autowired
    private GameHandlerService gameHandlerService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Accepts user requests to create a new game
     * The incoming data gets handled by {@link GameHandlerService#createNewGame(Player)}
     * @param player The requesting player
     * @return the ID of the new game
     */
    @MessageMapping("/create")
    @SendToUser(broadcast = false)
    public String createGame(@Header("simpUser") Player player) {
        return gameHandlerService.createNewGame(player);
    }

    /**
     * Accepts user requests to join a game.
     * The incoming data gets handled by {@link GameHandlerService#joinGame(Player, String)}
     * @param gameId The ID of the game to join
     * @param player The requesting player
     * @return Status whether joining was successful or not. See constants in {@link at.aau.se2.server.entity.Game}
     */
    @MessageMapping("/join")
    @SendToUser(broadcast = false)
    public String joinGame(@Payload String gameId, @Header("simpUser") Player player) {
        Integer code = gameHandlerService.joinGame(player, gameId);
        return code.toString(); // Send to the caller
    }
}
