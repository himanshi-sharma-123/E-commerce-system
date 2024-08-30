package com.example.ecommerce_system.dto.request;

import lombok.Data;

@Data
public class LoginUserRequest {

    private String username;
    private String password;
}
