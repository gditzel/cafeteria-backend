package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryTranslationDto;
import com.gonzalo.cafeteriabackend.domain.model.CategoryTranslation;

public class CategoryTranslationMapper {
    private CategoryTranslationMapper() {
    }

    public static CategoryTranslationDto toDto(CategoryTranslation translation) {
        if (translation == null) {
            return null;
        }
        CategoryTranslationDto dto = new CategoryTranslationDto();
        dto.setId(translation.getId());
        dto.setLanguageCode(translation.getLanguageCode());
        dto.setName(translation.getName());
        return dto;
    }

    public static CategoryTranslation toEntity(CategoryTranslationDto dto) {
        if (dto == null) {
            return null;
        }
        CategoryTranslation translation = new CategoryTranslation();
        translation.setId(dto.getId());
        translation.setLanguageCode(dto.getLanguageCode());
        translation.setName(dto.getName());
        return translation;
    }
}
