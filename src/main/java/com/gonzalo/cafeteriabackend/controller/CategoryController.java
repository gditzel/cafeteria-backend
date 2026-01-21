package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.Category;
import com.gonzalo.cafeteriabackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/active")
    public List<Category> getActive() {
        return categoryService.getActiveCategories();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.save(category);
    }

    // Endpoint para reordenar
    @PostMapping("/{id}/reorder")
    public void reorder(@PathVariable Long id, @RequestParam String direction) {
        categoryService.reorder(id, direction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}