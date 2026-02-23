package com.msd.myChatApp.Repositories;

import com.msd.myChatApp.Entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room,String> {
    Room getRoomByRoomId(String roomId);
}
