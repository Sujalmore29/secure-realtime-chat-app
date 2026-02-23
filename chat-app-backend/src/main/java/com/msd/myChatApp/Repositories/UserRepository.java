package com.msd.myChatApp.Repositories;

import com.msd.myChatApp.Entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Users,String> {

    Users findByUsername(String username);
}
