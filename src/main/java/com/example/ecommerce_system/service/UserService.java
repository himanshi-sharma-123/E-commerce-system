package com.example.ecommerce_system.service;

import com.example.ecommerce_system.dto.request.LoginUserRequest;
import com.example.ecommerce_system.dto.request.RegisterUserRequest;
import com.example.ecommerce_system.dto.response.LoginUserResponse;
import com.example.ecommerce_system.entity.User;
import com.example.ecommerce_system.enums.ResponseStatus;
import com.example.ecommerce_system.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public Optional<User> findByEmailOrUsername(String email, String username) {
        return userRepo.findByEmailOrUsername(email,username);
    }

    public User register(RegisterUserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setUserType(userRequest.getUserType());
        return userRepo.save(user);
    }

    public LoginUserResponse verify(LoginUserRequest userRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        LoginUserResponse response = new LoginUserResponse();

        try{
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userRequest.getUsername());
                response.setMessage("Login successful! Token: " + token);
                response.setResponseStatus(ResponseStatus.SUCCESS);
            }else{
                response.setMessage("Login failed. Invalid credentials.");
                response.setResponseStatus(ResponseStatus.FAILED);
            }
        }
            catch(Exception e){
                response.setMessage("Unexpected error!!");
                response.setResponseStatus(ResponseStatus.FAILED);
            }
        return response;
    }
}
