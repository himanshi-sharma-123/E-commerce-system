package com.example.ecommerce_system.dto.response;

import lombok.Data;

@Data
public class CartAddResponse extends BaseResponse{

    private Integer id;
    private String message;
}
