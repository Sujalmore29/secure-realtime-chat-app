package com.msd.myChatApp.Config;

import com.msd.myChatApp.Services.RoomServices;
import com.msd.myChatApp.Utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private final RoomServices roomServices;

    private static final Pattern SEND_DESTINATION = Pattern.compile("^/app/sendMessage/([^/]+)$");
    private static final Pattern TOPIC_DESTINATION = Pattern.compile("^/topic/room/([^/]+)$");

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            throw new IllegalArgumentException("Invalid STOMP message");
        }

        // 1. Authenticate the STOMP connection
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing JWT or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            if(!jwtUtils.validateToken(token)){
                throw new IllegalArgumentException("Invalid JWT");
            }
            String username = jwtUtils.extractUsername(token);

            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException(
                        "Invalid token identity");
            }

            accessor.setUser(new UsernamePasswordAuthenticationToken(username,null, List.of()));
        }

        // 2. Require an authenticated identity for protected frames
        if(StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal principal = accessor.getUser();

            if(principal == null){
                throw new IllegalArgumentException("Unauthenticated WebSocket user");
            }

            String username = principal.getName();
            String destination = accessor.getDestination();

            if(destination == null){
                throw new IllegalArgumentException("Missing STOMP destination");
            }

            // 3. Check permission to send to a room
            if(StompCommand.SEND.equals(accessor.getCommand())) {
                Matcher matcher = SEND_DESTINATION.matcher(destination);

                if(matcher.matches()) {
                    String roomId = matcher.group(1);

                    roomServices.requireMember(roomId, username);
                } else if (destination.startsWith("/app/")) {
                    throw new IllegalArgumentException("Unsupported messaging destination");
                }
            }

            // 4. Check permission to subscribe to a room
            if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                Matcher matcher = TOPIC_DESTINATION.matcher(destination);

                if(matcher.matches()) {
                    String roomId = matcher.group(1);
                    roomServices.requireMember(roomId, username);
                } else if(destination.startsWith("/topic/room/")) {
                    throw new IllegalArgumentException("Invalid room subscription");
                }
            }
        }
        return message;
    }
}
