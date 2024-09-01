package com.example.ecommerce_system.dto.response;

import com.example.ecommerce_system.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class BuyResponse extends BaseResponse{
    List<Product> products;
    int totalAmount;
    String message;
}
