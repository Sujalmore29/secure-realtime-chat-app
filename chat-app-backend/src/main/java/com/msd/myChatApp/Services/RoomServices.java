package com.msd.myChatApp.Services;

import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.Repositories.RoomRepository;
import com.msd.myChatApp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServices {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public boolean createRoom(String roomId, String username){
        try {
            if (roomId == null || roomId.isBlank()) {
                throw new IllegalArgumentException("Room ID is required");
            }

            if (roomRepository.findByRoomId(roomId).isPresent()) {
                throw new IllegalArgumentException("Room already exists");
            }

            Room room = new Room();
            room.setRoomId(roomId);
            room.setOwnerUsername(username);
            room.setMembers(new HashSet<>(Set.of(username)));
            roomRepository.save(room);
        } catch (Exception e){
            log.error("Exception occurred while saving room", e);
            return false;
        }
        return true;
    }


    public Room getRoom(String roomId){
        return roomRepository.findByRoomId(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    public boolean roomExistsById(String roomId){
        return roomRepository.existsByRoomId(roomId);
    }
    public boolean isMember(String roomId, String username) {
        return roomRepository.findByRoomId(roomId)
                .map(room ->
                        room.getMembers() != null
                                && room.getMembers().contains(username))
                .orElse(false);
    }

    public boolean isOwner(String roomId, String username) {
        return roomRepository.findByRoomId(roomId)
                .map(room ->
                        username.equals(room.getOwnerUsername()))
                .orElse(false);
    }

    public boolean requireMember(String roomId, String username) {
        if (!isMember(roomId, username)) {
            return false;
        }
        return true;
    }

    public Room addMember(
            String roomId,
            String requester,
            String newUsername) {

        requireOwner(roomId, requester);

        if (newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required");
        }

        Users user = userRepository.findByUsername(newUsername);

        Room room = getRoom(roomId);

        if (room.getMembers() == null) {
            room.setMembers(new HashSet<>());
        }

        room.getMembers().add(user.getUsername());

        return roomRepository.save(room);
    }

    public Room removeMember(
            String roomId,
            String requester,
            String usernameToRemove) {

        requireOwner(roomId, requester);

        Room room = getRoom(roomId);

        if (usernameToRemove.equals(room.getOwnerUsername())) {
            throw new IllegalArgumentException(
                    "The owner cannot be removed");
        }

        if (room.getMembers() == null
                || !room.getMembers().remove(usernameToRemove)) {
            throw new IllegalArgumentException(
                    "User is not a member of this room");
        }

        return roomRepository.save(room);
    }

    public Set<String> getMembers(
            String roomId,
            String requester) {

        requireMember(roomId, requester);

        Room room = getRoom(roomId);

        return room.getMembers() == null
                ? Set.of()
                : Set.copyOf(room.getMembers());
    }

    public List<Room> getRoomsForUser(String username) {
        return roomRepository.findAll().stream()
                .filter(room ->
                        room.getMembers() != null
                                && room.getMembers().contains(username))
                .toList();
    }

    private void requireOwner(
            String roomId,
            String username) {

        if (!isOwner(roomId, username)) {
            throw new SecurityException(
                    "Only the room owner can manage members");
        }
    }
    public void saveRoom(Room room){
        roomRepository.save(room);
    }
}
