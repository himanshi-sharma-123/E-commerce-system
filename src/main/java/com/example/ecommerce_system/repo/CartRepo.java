package com.example.ecommerce_system.repo;

import com.example.ecommerce_system.entity.Cart;
import com.example.ecommerce_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);

}
