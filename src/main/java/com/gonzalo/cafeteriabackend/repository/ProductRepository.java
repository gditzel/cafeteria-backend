package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Filtra por el campo boolean isActive
    List<Product> findByIsActiveTrue();
}