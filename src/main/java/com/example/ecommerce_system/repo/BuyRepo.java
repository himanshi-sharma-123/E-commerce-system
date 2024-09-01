package com.example.ecommerce_system.repo;

import com.example.ecommerce_system.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyRepo extends JpaRepository<Buy, Integer> {
    List<Buy> findByUserUsername(String username);

}
