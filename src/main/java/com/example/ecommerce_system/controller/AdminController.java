package com.example.ecommerce_system.controller;

import com.example.ecommerce_system.dto.request.CategoryAddRequest;
import com.example.ecommerce_system.dto.request.ProductAddRequest;
import com.example.ecommerce_system.dto.response.CategoryAddResponse;
import com.example.ecommerce_system.dto.response.GetProductResponse;
import com.example.ecommerce_system.dto.response.ProductAddResponse;
import com.example.ecommerce_system.entity.Category;
import com.example.ecommerce_system.entity.Product;
import com.example.ecommerce_system.entity.UserPrincipal;
import com.example.ecommerce_system.enums.ResponseStatus;
import com.example.ecommerce_system.enums.UserType;
import com.example.ecommerce_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/api/category/add")
    public CategoryAddResponse categoryAdd(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CategoryAddRequest categoryAddRequest){
        CategoryAddResponse response = new CategoryAddResponse();

        if(userPrincipal.getUserType() != UserType.ADMIN){
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("You are not authorized to perform this action.");
            return response;
        }

        try{
            Category addCategory = adminService.addCategory(categoryAddRequest);
            response.setId(addCategory.getId());
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Category added successfully.");
        }catch (Exception e){
            e.printStackTrace();
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("Failed to add category.");
        }
        return response;
    }

    @GetMapping("/api/category")
    public List<Category> getCategories(){
        return adminService.getCategories();
    }

    @PostMapping("/api/product/add")
    public ProductAddResponse productAdd(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductAddRequest productAddRequest){
        ProductAddResponse response = new ProductAddResponse();

        if(userPrincipal.getUserType() != UserType.ADMIN){
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("You are not authorized to perform this action.");
            return response;
        }

        try{
            Product addProduct = adminService.addProduct(productAddRequest);
            response.setId(addProduct.getId());
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Product added successfully.");
        }catch (Exception e){
            e.printStackTrace();
            response.setResponseStatus(ResponseStatus.FAILED);
            response.setMessage("Failed to add product.");
        }
        return response;
    }

    @GetMapping("/api/product")
    public List<GetProductResponse> getProducts(){
        return adminService.getProducts();
    }
}
