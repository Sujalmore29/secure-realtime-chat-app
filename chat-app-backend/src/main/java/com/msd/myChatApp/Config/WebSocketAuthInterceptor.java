package com.msd.myChatApp.Config;

import com.msd.myChatApp.Utils.JwtUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    public WebSocketAuthInterceptor(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing JWT");
            }

            String token = authHeader.substring(7);
            String username = jwtUtils.extractUsername(token);

            accessor.setUser(() -> username);
        }
        return message;
    }
}
