package com.example.ecommerce_system.dto.response;

import com.example.ecommerce_system.enums.ResponseStatus;
import lombok.Data;

@Data
public class BaseResponse {
    ResponseStatus responseStatus;
}
