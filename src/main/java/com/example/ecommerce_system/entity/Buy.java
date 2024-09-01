package com.example.ecommerce_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Buy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Cart cart;

    @ManyToOne
    private User user;

    private int amount;
}
