package com.msd.myChatApp.Controllers;

import com.msd.myChatApp.Entities.Message;
import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.PayLoad.MessageRequest;
import com.msd.myChatApp.Services.RoomServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RoomServices roomServices;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage/{roomId}") //app/sendMessage/roomId
    public void sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request,
            Principal principal) {

        if (principal == null) {
            throw new SecurityException("Authentication required");
        }

        if (request == null || request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Message content is required");
        }

        if (request.getContent().length() > 5000) {
            throw new IllegalArgumentException("Message is too long");
        }


        String sender = principal.getName();

        roomServices.requireMember(roomId, sender);
        Room room = roomServices.getRoom(roomId);

        Message message = new Message();
        message.setSender(sender);
        message.setContent(request.getContent().trim());
        message.setTimeStamp(LocalDateTime.now());

        if(room != null){
            room.getMessages().add(message);
            roomServices.saveRoom(room);
        }else{
            throw new RuntimeException("Room Not Found!");
        }
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

}
