package com.example.ecommerce_system.dto.response;

import lombok.Data;

@Data
public class GetProductResponse {
    private int id;
    private String productName;
    private int stock;
    private Integer price;
    private int categoryId;
}
