package com.gonzalo.cafeteriabackend.application.port.out;

import com.gonzalo.cafeteriabackend.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    List<Product> findAllOrderBySortOrderAsc();

    List<Product> findActive();

    List<Product> findActiveOrderBySortOrderAsc();

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Product save(Product product);

    void deleteById(Long id);

    void flush();
}
