package com.example.ecommerce_system.service;

import com.example.ecommerce_system.dto.request.CategoryAddRequest;
import com.example.ecommerce_system.dto.request.ProductAddRequest;
import com.example.ecommerce_system.entity.Category;
import com.example.ecommerce_system.entity.Product;
import com.example.ecommerce_system.repo.CategoryRepo;
import com.example.ecommerce_system.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    public Category addCategory(CategoryAddRequest categoryAddRequest) {
        Category category =new Category();
        category.setCategoryName(categoryAddRequest.getCategoryName());

        return categoryRepo.save(category);
    }

    public Product addProduct(ProductAddRequest productAddRequest) {
        Category category = categoryRepo.findById(productAddRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product =new Product();
        product.setProductName(productAddRequest.getProductName());
        product.setStock(productAddRequest.getStock());
        product.setPrice(productAddRequest.getPrice());
        product.setCategory(category);

        return productRepo.save(product);
    }
}
