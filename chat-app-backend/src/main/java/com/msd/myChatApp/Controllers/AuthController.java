package com.msd.myChatApp.Controllers;

import com.msd.myChatApp.Entities.Users;
import com.msd.myChatApp.Repositories.UserRepository;
import com.msd.myChatApp.Services.UserServices;
import com.msd.myChatApp.Utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServices userServices;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserServices userServices,JwtUtils jwtUtils,PasswordEncoder passwordEncoder){
        this.userServices = userServices;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users users){
        try{
            Users user = userServices.getUser(users.getUsername());
            if(user == null) {
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                userServices.saveUser(users);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("User already exists",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println("Error occured while registering user");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users){
        try{
            Users userInDb = userServices.getUser(users.getUsername());
            if(userInDb == null){
                throw new RuntimeException("User Not Found");
            }

            if(!passwordEncoder.matches(users.getPassword(),userInDb.getPassword())){
                throw new RuntimeException("Invalid Credentials");
            }
            String token = jwtUtils.generateToken(userInDb.getUsername());
            return new ResponseEntity<>(token,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error Occurred while login" + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
