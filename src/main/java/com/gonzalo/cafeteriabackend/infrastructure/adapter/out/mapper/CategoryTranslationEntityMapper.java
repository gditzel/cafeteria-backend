package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.CategoryTranslation;

public class CategoryTranslationEntityMapper {
    private CategoryTranslationEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation toEntity(
            CategoryTranslation domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation();
        entity.setId(domain.getId());
        entity.setLanguageCode(domain.getLanguageCode());
        entity.setName(domain.getName());
        return entity;
    }

    public static CategoryTranslation toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation entity) {
        if (entity == null) {
            return null;
        }
        CategoryTranslation domain = new CategoryTranslation();
        domain.setId(entity.getId());
        domain.setLanguageCode(entity.getLanguageCode());
        domain.setName(entity.getName());
        return domain;
    }
}
