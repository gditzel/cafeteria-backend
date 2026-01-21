package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.Category;
import com.gonzalo.cafeteriabackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        // Usamos el nuevo método del repositorio para traerlas ya ordenadas
        List<Category> categories = categoryRepository.findAllOrderBySortOrder();
        categories.forEach(c -> {
            if (c.getProducts() != null) c.getProducts().size();
        });
        return categories;
    }

    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActiveTrueOrderBySortOrderAsc();
    }

    @Transactional
    public Category save(Category category) {
        if (category.getId() == null) {
            // Asignar el último lugar a las nuevas
            List<Category> all = categoryRepository.findAll();
            int maxOrder = all.stream()
                    .mapToInt(c -> c.getSortOrder() != null ? c.getSortOrder() : 0)
                    .max()
                    .orElse(0);
            category.setSortOrder(maxOrder + 1);
        }

        if (category.getTranslations() != null) {
            category.getTranslations().forEach(t -> t.setCategory(category));
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void reorder(Long id, String direction) {
        List<Category> categories = categoryRepository.findAllOrderBySortOrder();
        Category current = categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (current == null) return;

        int index = categories.indexOf(current);

        // Lógica de intercambio basada en la posición real de la lista
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

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}