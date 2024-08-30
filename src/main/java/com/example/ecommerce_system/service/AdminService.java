package com.example.ecommerce_system.service;

import com.example.ecommerce_system.dto.request.CategoryAddRequest;
import com.example.ecommerce_system.entity.Category;
import com.example.ecommerce_system.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private CategoryRepo categoryRepo;
    public Category addCategory(CategoryAddRequest categoryAddRequest) {
        Category category =new Category();
        category.setCategoryName(categoryAddRequest.getCategoryName());

        return categoryRepo.save(category);
    }
}
