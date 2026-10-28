package com.msd.myChatApp.Controllers;

import com.msd.myChatApp.Entities.Message;
import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.PayLoad.RoomDetails;
import com.msd.myChatApp.Repositories.UserRepository;
import com.msd.myChatApp.Services.RoomServices;
import com.msd.myChatApp.Services.UserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    private final RoomServices roomServices;
    private final UserServices userServices;

    public record AddMemberRequest(String username) {}
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDetails roomDetails){
        try{
            boolean room = roomServices.createRoom(roomDetails.getRoomId(), roomDetails.getUsername());
            if(room){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }catch (Exception e) {
            return new ResponseEntity<>("Room already exists!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("Room created Successfully");
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(username != null) {
            return ResponseEntity.ok(username);
        }
        return ResponseEntity.badRequest().body("Error occurred while fetching user");
    }
    @GetMapping("/get/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if(!roomServices.roomExistsById(roomId)){
                throw new IllegalArgumentException("Room does not exists");
            }
            if(!roomServices.requireMember(roomId, username)){
                throw new SecurityException("You are not a member of this room");
            }
            Users user = userServices.getUser(username);
            if (user != null) {
                Room room = roomServices.getRoom(roomId);
                if (room != null) {
                    return new ResponseEntity<>(room, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception occurred while joining room",e);
            return new ResponseEntity<>("Error occurred: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Error occurred while joining room",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId,
                                         @RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                         @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Room room = roomServices.getRoom(roomId);
            if(room == null){
                return ResponseEntity.badRequest().build();
            }
            roomServices.requireMember(roomId, authentication.getName());
            //get messages
            //pagination
            List<Message> messages = room.getMessages();
            int start = Math.max(0, messages.size() - (page + 1) * size);
            int end = Math.min(messages.size(), start + size);
            List<Message> paginationMessages = messages.subList(start,end);
            return ResponseEntity.ok(paginationMessages);
        } catch (Exception e){
            log.error("Exception occurred while fetching messages",e);
        }
        return ResponseEntity.badRequest().body("Error getting messages");
    }

    @PostMapping("/{roomId}/members/add-member")
    public ResponseEntity<?> addMember(
            @PathVariable String roomId,
            @RequestBody AddMemberRequest request,
            Authentication authentication) {

        try {
            Room room = roomServices.addMember(
                    roomId,
                    authentication.getName(),
                    request.username());

            return ResponseEntity.ok(room.getMembers());

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}/members/remove-member/{username}")
    public ResponseEntity<?> removeMember(
            @PathVariable String roomId,
            @PathVariable String username,
            Authentication authentication) {

        try {
            roomServices.removeMember(
                    roomId,
                    authentication.getName(),
                    username);

            return ResponseEntity.noContent().build();

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{roomId}/members")
    public ResponseEntity<?> getMembers(@PathVariable String roomId, Authentication authentication) {

        try {
            Set<String> members = roomServices.getMembers(
                    roomId, authentication.getName());

            return ResponseEntity.ok(members);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(
            @PathVariable String roomId,
            Authentication authentication) {

        try {
            roomServices.requireMember(roomId, authentication.getName());

            return ResponseEntity.ok(roomServices.getRoom(roomId));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
