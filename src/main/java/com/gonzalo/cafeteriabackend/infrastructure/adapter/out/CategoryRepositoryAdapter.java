package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.CategoryRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.CategoryRepository;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper.CategoryEntityMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {
    private final CategoryRepository categoryRepository;

    public CategoryRepositoryAdapter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllOrderBySortOrder() {
        return categoryRepository.findAllOrderBySortOrder().stream()
                .map(CategoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findActiveOrderBySortOrder() {
        return categoryRepository.findByIsActiveTrueOrderBySortOrderAsc().stream()
                .map(CategoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Category save(Category category) {
        return CategoryEntityMapper.toDomain(
                categoryRepository.save(CategoryEntityMapper.toEntity(category)));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
