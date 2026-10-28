package com.msd.myChatApp.Services;

import com.msd.myChatApp.Entities.Room;
import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepository userRepository;

    public Users getUser(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(Users user){
        userRepository.save(user);
    }
}
