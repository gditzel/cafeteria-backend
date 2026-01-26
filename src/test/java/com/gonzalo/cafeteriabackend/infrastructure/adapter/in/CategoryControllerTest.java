package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalo.cafeteriabackend.application.port.in.category.DeleteCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetActiveCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.GetAllCategories;
import com.gonzalo.cafeteriabackend.application.port.in.category.ReorderCategory;
import com.gonzalo.cafeteriabackend.application.port.in.category.SaveCategory;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetAllCategories getAllCategories;

    @MockBean
    private GetActiveCategories getActiveCategories;

    @MockBean
    private SaveCategory saveCategory;

    @MockBean
    private ReorderCategory reorderCategory;

    @MockBean
    private DeleteCategory deleteCategory;

    @Test
    void getAllReturnsCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setIsActive(true);
        category.setSortOrder(2);

        when(getAllCategories.execute(any(GetAllCategories.GetAllCategoriesRequest.class)))
                .thenReturn(new GetAllCategories.GetAllCategoriesResponse(List.of(category)));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[0].sortOrder").value(2));
    }

    @Test
    void getActiveReturnsCategories() throws Exception {
        Category category = new Category();
        category.setId(2L);
        category.setIsActive(true);

        when(getActiveCategories.execute(any(GetActiveCategories.GetActiveCategoriesRequest.class)))
                .thenReturn(new GetActiveCategories.GetActiveCategoriesResponse(List.of(category)));

        mockMvc.perform(get("/api/categories/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void createReturnsSavedCategory() throws Exception {
        Category saved = new Category();
        saved.setId(5L);
        saved.setIsActive(true);
        saved.setSortOrder(1);

        when(saveCategory.execute(any(SaveCategory.SaveCategoryRequest.class)))
                .thenReturn(new SaveCategory.SaveCategoryResponse(saved));

        CategoryDto dto = new CategoryDto();
        dto.setIsActive(true);
        dto.setSortOrder(1);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.sortOrder").value(1));
    }

    @Test
    void reorderCallsUseCase() throws Exception {
        mockMvc.perform(post("/api/categories/7/reorder")
                        .param("direction", "up"))
                .andExpect(status().isOk());

        verify(reorderCategory).execute(new ReorderCategory.ReorderCategoryRequest(7L, "up"));
    }

    @Test
    void deleteCallsUseCase() throws Exception {
        mockMvc.perform(delete("/api/categories/4"))
                .andExpect(status().isOk());

        verify(deleteCategory).execute(new DeleteCategory.DeleteCategoryRequest(4L));
    }
}
