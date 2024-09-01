package com.example.ecommerce_system.dto.response;

import com.example.ecommerce_system.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private Integer orderId;
    private int amount;
    private List<Product> products;
}

