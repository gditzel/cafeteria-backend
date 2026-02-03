package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.category.DeleteCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetActiveCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetAllCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.ReorderCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.SaveCategory;
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
    private final GetAllCategories getAllCategories;
    private final GetActiveCategories getActiveCategories;
    private final SaveCategory saveCategory;
    private final ReorderCategory reorderCategory;
    private final DeleteCategory deleteCategory;

    public CategoryController(
            GetAllCategories getAllCategories,
            GetActiveCategories getActiveCategories,
            SaveCategory saveCategory,
            ReorderCategory reorderCategory,
            DeleteCategory deleteCategory) {
        this.getAllCategories = getAllCategories;
        this.getActiveCategories = getActiveCategories;
        this.saveCategory = saveCategory;
        this.reorderCategory = reorderCategory;
        this.deleteCategory = deleteCategory;
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        var response = getAllCategories.execute(new GetAllCategories.GetAllCategoriesRequest());
        return response.categories().stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @GetMapping("/active")
    public List<CategoryDto> getActive() {
        var response = getActiveCategories.execute(new GetActiveCategories.GetActiveCategoriesRequest());
        return response.categories().stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto category) {
        var response = saveCategory.execute(new SaveCategory.SaveCategoryRequest(CategoryMapper.toEntity(category)));
        return CategoryMapper.toDto(response.category());
    }

    @PostMapping("/{id}/reorder")
    public void reorder(@PathVariable Long id, @RequestParam String direction) {
        reorderCategory.execute(new ReorderCategory.ReorderCategoryRequest(id, direction));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteCategory.execute(new DeleteCategory.DeleteCategoryRequest(id));
    }
}
