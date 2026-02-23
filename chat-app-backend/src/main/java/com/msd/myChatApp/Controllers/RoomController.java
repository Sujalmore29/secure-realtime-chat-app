package com.msd.myChatApp.Controllers;

import com.msd.myChatApp.Entities.Message;
import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.Repositories.UserRepository;
import com.msd.myChatApp.Services.RoomServices;
import com.msd.myChatApp.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomServices roomServices;

    @Autowired
    private UserServices userServices;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){
        if(roomServices.createRoom(roomId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Room already exists!",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userServices.getUser(username);
        if(user != null) {
            Room room = roomServices.getRoom(roomId);
            if (room != null) {
                return new ResponseEntity<>(room, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Login Required",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId,
                                         @RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                         @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        Room room = roomServices.getRoom(roomId);
        if(room == null){
            return ResponseEntity.badRequest().build();
        }

        //get messages
        //pagination
        List<Message> messages = room.getMessages();
        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);
        List<Message> paginationMessages = messages.subList(start,end);
        return ResponseEntity.ok(messages);
    }
}
