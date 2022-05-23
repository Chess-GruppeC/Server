package at.aau.se2.server.config;

import at.aau.se2.server.entity.Game;
import at.aau.se2.server.entity.Player;
import at.aau.se2.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @EventListener
    public void webSocketSubscribeListener(SessionSubscribeEvent event) {
        String destination = (String) event.getMessage().getHeaders().get("simpDestination");
        if(destination != null && destination.startsWith("/topic/update")) {
            Player player = (Player) event.getUser();
            Objects.requireNonNull(player);
            player.setGameSubscribedTo(destination);
            Game game = gameRepository.findById(player.getGameSubscribedTo());
            // If the game was already started send the last state to the subscribing player
            if(gameRepository.findById(player.getGameSubscribedTo()).getGameState() != null) {
                simpMessagingTemplate.convertAndSend("/topic/update/" + player.getGameSubscribedTo(), game.getGameState());
            }
        }
    }

}