package com.gonzalo.cafeteriabackend.application.port.in.category;

import com.gonzalo.cafeteriabackend.domain.model.Category;
import java.util.List;

public interface GetActiveCategories {
    GetActiveCategoriesResponse execute(GetActiveCategoriesRequest request);

    record GetActiveCategoriesRequest() {}

    record GetActiveCategoriesResponse(List<Category> categories) {}
}
