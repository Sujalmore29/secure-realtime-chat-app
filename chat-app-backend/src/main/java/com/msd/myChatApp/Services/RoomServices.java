package com.msd.myChatApp.Services;

import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoomServices {
    @Autowired
    private RoomRepository roomRepository;

    public boolean createRoom(String roomId){
        try {
            Room roomByRoomId = roomRepository.getRoomByRoomId(roomId);
            if(roomByRoomId == null) {
                Room room = new Room();
                room.setRoomId(roomId);
                roomRepository.save(room);
                return true;
            }
        } catch (Exception e){
            System.out.println("Exception occurred while saving room" + e);
        }
        return false;
    }
    public Room getRoom(String roomId){
        return roomRepository.getRoomByRoomId(roomId);
    }
    public void saveRoom(Room room){
        roomRepository.save(room);
    }
}
