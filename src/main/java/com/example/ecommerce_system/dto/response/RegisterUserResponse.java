package com.example.ecommerce_system.dto.response;

import lombok.Data;

@Data
public class RegisterUserResponse extends BaseResponse{

    private Integer id;
    private String message;
}
