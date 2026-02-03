package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.Category;
import com.gonzalo.cafeteriabackend.domain.model.CategoryTranslation;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryEntityMapper {
    private CategoryEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Category toEntity(
            Category domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Category entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Category();
        entity.setId(domain.getId());
        entity.setIsActive(domain.getIsActive());
        entity.setSortOrder(domain.getSortOrder());
        entity.setTranslations(mapTranslationsToEntity(domain.getTranslations(), entity));
        return entity;
    }

    public static Category toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Category entity) {
        if (entity == null) {
            return null;
        }
        Category domain = new Category();
        domain.setId(entity.getId());
        domain.setIsActive(entity.getIsActive());
        domain.setSortOrder(entity.getSortOrder());
        domain.setTranslations(mapTranslationsToDomain(entity.getTranslations()));
        if (entity.getProducts() != null) {
            domain.setProductCount(entity.getProducts().size());
        }
        return domain;
    }

    private static List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation>
            mapTranslationsToEntity(
            List<CategoryTranslation> translations,
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Category categoryEntity) {
        if (translations == null) {
            return null;
        }
        List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation> mapped =
                translations.stream()
                .map(CategoryTranslationEntityMapper::toEntity)
                .collect(Collectors.toList());
        mapped.forEach(t -> t.setCategory(categoryEntity));
        return mapped;
    }

    private static List<CategoryTranslation> mapTranslationsToDomain(
            List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.CategoryTranslation> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(CategoryTranslationEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
