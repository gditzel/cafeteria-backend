package com.gonzalo.cafeteriabackend.application.port.in.category;

import com.gonzalo.cafeteriabackend.domain.model.Category;
import java.util.List;

public interface GetAllCategories {
    GetAllCategoriesResponse execute(GetAllCategoriesRequest request);

    record GetAllCategoriesRequest() {}

    record GetAllCategoriesResponse(List<Category> categories) {}
}
