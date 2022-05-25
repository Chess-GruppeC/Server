package at.aau.se2.server.controller;

import at.aau.se2.server.dto.PlayerDTO;
import at.aau.se2.server.dto.DiceResultDTO;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.mapper.PlayerMapper;
import at.aau.se2.server.service.GameUpdateService;
import at.aau.se2.server.service.GameHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameUpdateService gameUpdateService;

    @Autowired
    private GameHandlerService gameHandlerService;

    /**
     * Passes the incoming data to the {@link GameUpdateService#onUpdate(Object, Player, String)} method to handle and broadcasts the returned value to all players
     * subscribed to the destination game ID
     *
     * @param gameId  the game id
     * @param payload The game data
     */
    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/update/{gameId}")
    public String gameUpdate(@DestinationVariable String gameId, @Header("simpUser") Player player, @Payload String payload) {
        return gameUpdateService.onUpdate(payload, player, gameId);
    }

    @MessageMapping("/game/opponent")
    @SendToUser(broadcast = false)
    public PlayerDTO getOpponent(@Payload String gameId, @Header("simpUser") Player requestingPlayer) {
        return gameHandlerService.getOpponentOf(requestingPlayer, gameId);
    }

    @MessageMapping("/game/rollDice/{gameId}")
    @SendTo("/topic/getStartingPlayer/{gameId}")
    public DiceResultDTO getStartingPlayerByDiceValue(@DestinationVariable String gameId, @Payload Integer diceValue, @Header("simpUser") Player requestingPlayer) {
        return gameHandlerService.setDiceValueAndCompare(requestingPlayer, gameId, diceValue);
    }

    @SubscribeMapping("/update/{gameId}")
    public String getGameState(@DestinationVariable String gameId, @Header("simpUser") Player player) {
        return gameUpdateService.onSubscribed(gameId, player);
    }

}
