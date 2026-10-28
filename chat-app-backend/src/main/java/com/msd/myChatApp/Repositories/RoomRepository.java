package com.msd.myChatApp.Repositories;

import com.msd.myChatApp.Entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room,String> {
    Room getRoomByRoomId(String roomId);
    Optional<Room> findByRoomId(String roomId);
    boolean existsByRoomId(String roomId);
}
