package com.gonzalo.cafeteriabackend.application.port.in.category;

public interface DeleteCategory {
    DeleteCategoryResponse execute(DeleteCategoryRequest request);

    record DeleteCategoryRequest(Long id) {}

    record DeleteCategoryResponse(boolean deleted) {}
}
