package com.example.ecommerce_system.service;

import com.example.ecommerce_system.dto.request.CategoryAddRequest;
import com.example.ecommerce_system.dto.request.ProductAddRequest;
import com.example.ecommerce_system.dto.response.GetProductResponse;
import com.example.ecommerce_system.entity.Category;
import com.example.ecommerce_system.entity.Product;
import com.example.ecommerce_system.repo.CategoryRepo;
import com.example.ecommerce_system.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    public List<GetProductResponse> getProducts() {
        List<Product> products = productRepo.findAll();

        // Map entities to DTOs
        return products.stream().map(product -> {
            GetProductResponse dto = new GetProductResponse();
            dto.setId(product.getId());
            dto.setProductName(product.getProductName());
            dto.setStock(product.getStock());
            dto.setPrice(product.getPrice());
            dto.setCategoryId(product.getCategory().getId());  // Set the category ID
            return dto;
        }).collect(Collectors.toList());    }
}
