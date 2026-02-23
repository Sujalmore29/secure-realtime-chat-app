package com.msd.myChatApp.Services;

import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    public Users getUser(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(Users user){
        userRepository.save(user);
    }
}
