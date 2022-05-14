package at.aau.se2.server.controller;

import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.dto.DiceResultDTO;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.service.GameUpdateService;
import at.aau.se2.server.service.UpdateService;
import at.aau.se2.server.service.GameHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameUpdateService gameUpdateService;

    @Autowired
    private GameHandlerService gameHandlerService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PlayerMapper playerMapper;

    /**
     * Passes the incoming data to the {@link UpdateService#onUpdate(Object, String)} method to handle and broadcasts the returned value to all players
     * subscribed to the destination game ID
     *
     * @param gameId the game id
     * @param payload The game data
     * @return The updated game data to broadcast to the players
     */
    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/update/{gameId}")
    public String gameUpdate(@DestinationVariable String gameId, @Payload String payload) {
        return gameUpdateService.onUpdate(payload, gameId);
    }

    @MessageMapping("/game/opponent")
    @SendToUser(broadcast = false)
    public PlayerDTO getOpponent(@Payload String gameId, @Header("simpUser") Player requestingPlayer) {
        return gameHandlerService.getOpponentOf(requestingPlayer, gameId);
    }

    @MessageMapping("/game/rollDice/{gameId}")
    public void getStartingPlayerByDiceValue(@DestinationVariable String gameId, @Payload Integer diceValue, @Header("simpUser") Player requestingPlayer) {
        DiceResultDTO result = gameHandlerService.setDiceValueAndCompare(requestingPlayer, gameId, diceValue);
        if (result != null) {
            simpMessagingTemplate.convertAndSend("/topic/getStartingPlayer/" + gameId, result);
        }
    }

}
