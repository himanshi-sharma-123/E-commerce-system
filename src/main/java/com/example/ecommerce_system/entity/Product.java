package com.example.ecommerce_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productName;
    private Integer stock;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
