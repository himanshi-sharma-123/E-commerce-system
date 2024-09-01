package com.example.ecommerce_system.repo;

import com.example.ecommerce_system.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyRepo extends JpaRepository<Buy, Integer> {
}
