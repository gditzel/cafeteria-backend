package com.gonzalo.cafeteriabackend.application.port.in.category;

import com.gonzalo.cafeteriabackend.domain.model.Category;

public interface SaveCategory {
    SaveCategoryResponse execute(SaveCategoryRequest request);

    record SaveCategoryRequest(Category category) {}

    record SaveCategoryResponse(Category category) {}
}
