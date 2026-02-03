package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryTranslationDto;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import com.gonzalo.cafeteriabackend.domain.model.CategoryTranslation;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    private CategoryMapper() {
    }

    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setIsActive(category.getIsActive());
        dto.setSortOrder(category.getSortOrder());
        dto.setProductCount(category.getProductCount());
        dto.setTranslations(mapTranslationsToDto(category.getTranslations()));
        return dto;
    }

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setId(dto.getId());
        category.setIsActive(dto.getIsActive());
        category.setSortOrder(dto.getSortOrder());
        category.setTranslations(mapTranslationsToEntity(dto.getTranslations()));
        return category;
    }

    private static List<CategoryTranslationDto> mapTranslationsToDto(List<CategoryTranslation> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(CategoryTranslationMapper::toDto)
                .collect(Collectors.toList());
    }

    private static List<CategoryTranslation> mapTranslationsToEntity(List<CategoryTranslationDto> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(CategoryTranslationMapper::toEntity)
                .collect(Collectors.toList());
    }
}
