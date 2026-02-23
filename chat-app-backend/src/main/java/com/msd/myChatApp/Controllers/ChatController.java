package com.msd.myChatApp.Controllers;

import com.msd.myChatApp.Entities.Message;
import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.PayLoad.MessageRequest;
import com.msd.myChatApp.Services.RoomServices;
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
public class ChatController {
    @Autowired
    private RoomServices roomServices;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage/{roomId}") //app/sendMessage/roomId
    @SendTo("/topic/room/{roomId}") //iss pr subscribe krega (server jab push krega msg ko toh yaha pr aajaayega
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request,
            Principal principal) {

        String sender = principal.getName();

        Room room = roomServices.getRoom(request.getRoomId());

        Message message = new Message();
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setTimeStamp(LocalDateTime.now());

        if(room != null){
            room.getMessages().add(message);
            roomServices.saveRoom(room);
        }else{
            throw new RuntimeException("Room Not Found!");
        }
        return message;
    }

}
