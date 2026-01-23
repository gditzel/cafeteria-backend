package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.ProductTranslation;

public class ProductTranslationEntityMapper {
    private ProductTranslationEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation toEntity(
            ProductTranslation domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation();
        entity.setId(domain.getId());
        entity.setLanguageCode(domain.getLanguageCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        return entity;
    }

    public static ProductTranslation toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation entity) {
        if (entity == null) {
            return null;
        }
        ProductTranslation domain = new ProductTranslation();
        domain.setId(entity.getId());
        domain.setLanguageCode(entity.getLanguageCode());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        return domain;
    }
}
