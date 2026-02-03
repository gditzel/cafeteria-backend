package com.gonzalo.cafeteriabackend.application.usecase.category;

import com.gonzalo.cafeteriabackend.application.port.in.category.DeleteCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetActiveCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetAllCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.ReorderCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.SaveCategory;
import com.gonzalo.cafeteriabackend.application.port.out.CategoryRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryUseCases implements
        GetAllCategories,
        GetActiveCategories,
        SaveCategory,
        ReorderCategory,
        DeleteCategory {
    private final CategoryRepositoryPort categoryRepository;

    public CategoryUseCases(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllCategoriesResponse execute(GetAllCategoriesRequest request) {
        return new GetAllCategoriesResponse(categoryRepository.findAllOrderBySortOrder());
    }

    @Override
    @Transactional(readOnly = true)
    public GetActiveCategoriesResponse execute(GetActiveCategoriesRequest request) {
        return new GetActiveCategoriesResponse(categoryRepository.findActiveOrderBySortOrder());
    }

    @Override
    @Transactional
    public SaveCategoryResponse execute(SaveCategoryRequest request) {
        Category category = request.category();
        if (category.getId() == null) {
            List<Category> all = categoryRepository.findAll();
            int maxOrder = all.stream()
                    .mapToInt(c -> c.getSortOrder() != null ? c.getSortOrder() : 0)
                    .max()
                    .orElse(0);
            category.setSortOrder(maxOrder + 1);
        }

        return new SaveCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public ReorderCategoryResponse execute(ReorderCategoryRequest request) {
        List<Category> categories = categoryRepository.findAllOrderBySortOrder();
        Category current = categories.stream()
                .filter(c -> c.getId().equals(request.id()))
                .findFirst()
                .orElse(null);

        if (current == null) {
            return new ReorderCategoryResponse(false);
        }

        int index = categories.indexOf(current);
        boolean reordered = false;

        if ("up".equals(request.direction()) && index > 0) {
            Category previous = categories.get(index - 1);
            swapOrder(current, previous);
            reordered = true;
        } else if ("down".equals(request.direction()) && index < categories.size() - 1) {
            Category next = categories.get(index + 1);
            swapOrder(current, next);
            reordered = true;
        }

        return new ReorderCategoryResponse(reordered);
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
    public DeleteCategoryResponse execute(DeleteCategoryRequest request) {
        categoryRepository.deleteById(request.id());
        return new DeleteCategoryResponse(true);
    }
}
