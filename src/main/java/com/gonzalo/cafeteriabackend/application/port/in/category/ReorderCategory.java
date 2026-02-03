package com.gonzalo.cafeteriabackend.application.port.in.category;

public interface ReorderCategory {
    ReorderCategoryResponse execute(ReorderCategoryRequest request);

    record ReorderCategoryRequest(Long id, String direction) {}

    record ReorderCategoryResponse(boolean reordered) {}
}
