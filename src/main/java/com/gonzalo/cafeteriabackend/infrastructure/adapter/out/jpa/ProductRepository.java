package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Filtra por el campo boolean isActive y mantiene el orden de visualización
    List<Product> findByIsActiveTrueOrderBySortOrderAsc();

    List<Product> findByIsActiveTrue();
}
