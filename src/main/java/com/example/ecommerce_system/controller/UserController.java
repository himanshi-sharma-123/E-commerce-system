package com.example.ecommerce_system.controller;

import com.example.ecommerce_system.dto.request.CartAddRequest;
import com.example.ecommerce_system.dto.request.LoginUserRequest;
import com.example.ecommerce_system.dto.request.BuyRequest;
import com.example.ecommerce_system.dto.request.RegisterUserRequest;
import com.example.ecommerce_system.dto.response.*;
import com.example.ecommerce_system.entity.Cart;
import com.example.ecommerce_system.entity.User;
import com.example.ecommerce_system.enums.ResponseStatus;
import com.example.ecommerce_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest userRequest){
        RegisterUserResponse response = new RegisterUserResponse();
        try {
            Optional<User> existingUser = userService.findByEmailOrUsername(userRequest.getEmail(), userRequest.getUsername());

            if(existingUser.isPresent()){
                response.setResponseStatus(ResponseStatus.FAILED);
                response.setMessage("The User is already exist with the given username and password");
            }else {
                User registerUser = userService.register(userRequest);
                System.out.println(registerUser);
                response.setId(registerUser.getId());
                response.setResponseStatus(ResponseStatus.SUCCESS);
                response.setMessage("User registered successfully.");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("An error occurred during registration.");

        }
        return response;
    }

    @PostMapping("/api/login")
    public LoginUserResponse login(@RequestBody LoginUserRequest userRequest){
        return userService.verify(userRequest);
    }

    @PostMapping("/api/cart/add")
    public CartAddResponse addToCart(Authentication authentication, @RequestBody CartAddRequest cartAddRequest){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println(username);
        return userService.addToCart(username, cartAddRequest);
    }

    @GetMapping("/api/cart")
    public List<Cart> getCart(){
        return userService.getCart();
    }

    @GetMapping("/api/allUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/api/cart/buy")
    public BuyResponse buyProduct(Authentication authentication, @RequestBody BuyRequest orderRequest){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.buyProduct(userDetails.getUsername(),orderRequest);
    }

    @GetMapping("/api/orders")
    public List<OrderResponse> getUserOrders(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getOrdersForUser(userDetails.getUsername());
    }
}
