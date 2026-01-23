package com.gonzalo.cafeteriabackend.application.service;

import com.gonzalo.cafeteriabackend.application.port.in.CategoryUseCase;
import com.gonzalo.cafeteriabackend.application.port.out.CategoryRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService implements CategoryUseCase {
    private final CategoryRepositoryPort categoryRepository;

    public CategoryService(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAllOrderBySortOrder();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        return categoryRepository.findActiveOrderBySortOrder();
    }

    @Override
    @Transactional
    public Category save(Category category) {
        if (category.getId() == null) {
            List<Category> all = categoryRepository.findAll();
            int maxOrder = all.stream()
                    .mapToInt(c -> c.getSortOrder() != null ? c.getSortOrder() : 0)
                    .max()
                    .orElse(0);
            category.setSortOrder(maxOrder + 1);
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void reorder(Long id, String direction) {
        List<Category> categories = categoryRepository.findAllOrderBySortOrder();
        Category current = categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (current == null) {
            return;
        }

        int index = categories.indexOf(current);

        if ("up".equals(direction) && index > 0) {
            Category previous = categories.get(index - 1);
            swapOrder(current, previous);
        } else if ("down".equals(direction) && index < categories.size() - 1) {
            Category next = categories.get(index + 1);
            swapOrder(current, next);
        }
    }

    private void swapOrder(Category c1, Category c2) {
        Integer temp = c1.getSortOrder();
        c1.setSortOrder(c2.getSortOrder());
        c2.setSortOrder(temp);
        categoryRepository.save(c1);
        categoryRepository.save(c2);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
