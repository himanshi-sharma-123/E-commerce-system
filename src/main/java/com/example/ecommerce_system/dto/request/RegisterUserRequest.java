package com.example.ecommerce_system.dto.request;

import com.example.ecommerce_system.enums.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RegisterUserRequest {

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
