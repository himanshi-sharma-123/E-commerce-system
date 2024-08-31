package com.example.ecommerce_system.entity;

import com.example.ecommerce_system.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(mappedBy = "user")
    @JsonIgnore // Prevents infinite loop with Cart -> User -> Cart
    private Cart cart;
}
