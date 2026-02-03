package com.gonzalo.cafeteriabackend.application.port.out;

import com.gonzalo.cafeteriabackend.domain.model.Category;
import java.util.List;

public interface CategoryRepositoryPort {
    List<Category> findAllOrderBySortOrder();

    List<Category> findActiveOrderBySortOrder();

    List<Category> findAll();

    Category save(Category category);

    void deleteById(Long id);
}
