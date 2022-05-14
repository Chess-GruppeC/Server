package at.aau.se2.server.config;

import at.aau.se2.server.entity.Player;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;

import java.util.ArrayList;
import java.util.Map;

public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            Object raw = message.getHeaders().get(NativeMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("username");

                if (name instanceof ArrayList) {
                    String username = ((ArrayList<String>) name).get(0);
                    Player newPlayer = new Player(username);
                    newPlayer.setSessionId(accessor.getSessionId());
                    accessor.setUser(newPlayer);
                }
            }
        }
        return message;
    }
}