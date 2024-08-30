package com.example.ecommerce_system.dto.request;

import lombok.Data;

@Data
public class CartAddRequest {

    private Integer productId;
    private Integer cartId;
}
