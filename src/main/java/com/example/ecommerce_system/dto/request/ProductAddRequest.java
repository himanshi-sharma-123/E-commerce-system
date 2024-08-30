package com.example.ecommerce_system.dto.request;

import com.example.ecommerce_system.entity.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ProductAddRequest {

    private String productName;
    private Integer stock;
    private Integer price;
    private Integer categoryId;
}
