package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.CategoryUseCase;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.CategoryMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    private final CategoryUseCase categoryUseCase;

    public CategoryController(CategoryUseCase categoryUseCase) {
        this.categoryUseCase = categoryUseCase;
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryUseCase.getAll().stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @GetMapping("/active")
    public List<CategoryDto> getActive() {
        return categoryUseCase.getActiveCategories().stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto category) {
        return CategoryMapper.toDto(categoryUseCase.save(CategoryMapper.toEntity(category)));
    }

    @PostMapping("/{id}/reorder")
    public void reorder(@PathVariable Long id, @RequestParam String direction) {
        categoryUseCase.reorder(id, direction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryUseCase.delete(id);
    }
}
