package com.gonzalo.cafeteriabackend.application.port.in;

import com.gonzalo.cafeteriabackend.domain.model.Category;
import java.util.List;

public interface CategoryUseCase {
    List<Category> getAll();

    List<Category> getActiveCategories();

    Category save(Category category);

    void reorder(Long id, String direction);

    void delete(Long id);
}
