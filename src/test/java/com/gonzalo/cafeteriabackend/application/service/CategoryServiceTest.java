package com.gonzalo.cafeteriabackend.application.usecase.category;

import com.gonzalo.cafeteriabackend.application.port.in.category.DeleteCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetActiveCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetAllCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.ReorderCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.SaveCategory;
import com.gonzalo.cafeteriabackend.application.port.out.CategoryRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryUseCasesTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private CategoryUseCases categoryUseCases;

    @Test
    void getAllUsesRepositoryOrder() {
        Category category = new Category();
        when(categoryRepository.findAllOrderBySortOrder()).thenReturn(List.of(category));

        List<Category> result = categoryUseCases
                .execute(new GetAllCategories.GetAllCategoriesRequest())
                .categories();

        assertThat(result).containsExactly(category);
    }

    @Test
    void getActiveCategoriesUsesRepositoryOrder() {
        Category category = new Category();
        when(categoryRepository.findActiveOrderBySortOrder()).thenReturn(List.of(category));

        List<Category> result = categoryUseCases
                .execute(new GetActiveCategories.GetActiveCategoriesRequest())
                .categories();

        assertThat(result).containsExactly(category);
    }

    @Test
    void saveNewCategoryAssignsNextSortOrder() {
        Category existing1 = new Category();
        existing1.setSortOrder(2);
        Category existing2 = new Category();
        existing2.setSortOrder(5);
        when(categoryRepository.findAll()).thenReturn(List.of(existing1, existing2));

        Category toSave = new Category();

        categoryUseCases.execute(new SaveCategory.SaveCategoryRequest(toSave));

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        assertThat(captor.getValue().getSortOrder()).isEqualTo(6);
    }

    @Test
    void saveExistingCategoryDoesNotRecalculateSortOrder() {
        Category toSave = new Category();
        toSave.setId(10L);
        toSave.setSortOrder(3);

        categoryUseCases.execute(new SaveCategory.SaveCategoryRequest(toSave));

        verify(categoryRepository, never()).findAll();
        verify(categoryRepository).save(toSave);
    }

    @Test
    void reorderUpSwapsOrders() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setSortOrder(1);
        Category c2 = new Category();
        c2.setId(2L);
        c2.setSortOrder(2);
        Category c3 = new Category();
        c3.setId(3L);
        c3.setSortOrder(3);
        when(categoryRepository.findAllOrderBySortOrder()).thenReturn(List.of(c1, c2, c3));

        categoryUseCases.execute(new ReorderCategory.ReorderCategoryRequest(2L, "up"));

        assertThat(c1.getSortOrder()).isEqualTo(2);
        assertThat(c2.getSortOrder()).isEqualTo(1);
        verify(categoryRepository).save(c1);
        verify(categoryRepository).save(c2);
    }

    @Test
    void reorderNoopWhenCategoryMissing() {
        when(categoryRepository.findAllOrderBySortOrder()).thenReturn(List.of());

        categoryUseCases.execute(new ReorderCategory.ReorderCategoryRequest(99L, "up"));

        verify(categoryRepository, never()).save(org.mockito.ArgumentMatchers.any(Category.class));
    }

    @Test
    void deleteDelegatesToRepository() {
        categoryUseCases.execute(new DeleteCategory.DeleteCategoryRequest(7L));

        verify(categoryRepository).deleteById(7L);
    }
}
