package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.ProductTranslationDto;
import com.gonzalo.cafeteriabackend.domain.model.ProductTranslation;

public class ProductTranslationMapper {
    private ProductTranslationMapper() {
    }

    public static ProductTranslationDto toDto(ProductTranslation translation) {
        if (translation == null) {
            return null;
        }
        ProductTranslationDto dto = new ProductTranslationDto();
        dto.setId(translation.getId());
        dto.setLanguageCode(translation.getLanguageCode());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        return dto;
    }

    public static ProductTranslation toEntity(ProductTranslationDto dto) {
        if (dto == null) {
            return null;
        }
        ProductTranslation translation = new ProductTranslation();
        translation.setId(dto.getId());
        translation.setLanguageCode(dto.getLanguageCode());
        translation.setName(dto.getName());
        translation.setDescription(dto.getDescription());
        return translation;
    }
}
