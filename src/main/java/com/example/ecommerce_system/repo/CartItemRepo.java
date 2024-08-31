package com.example.ecommerce_system.repo;

import com.example.ecommerce_system.entity.Cart;
import com.example.ecommerce_system.entity.CartItem;
import com.example.ecommerce_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
    CartItem findByCartAndProduct(Cart cart, Product product);
}
