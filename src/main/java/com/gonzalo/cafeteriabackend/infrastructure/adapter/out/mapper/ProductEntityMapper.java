package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.domain.model.ProductTranslation;
import java.util.List;
import java.util.stream.Collectors;

public class ProductEntityMapper {
    private ProductEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product toEntity(
            Product domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product();
        entity.setId(domain.getId());
        entity.setIsActive(domain.getIsActive());
        entity.setSortOrder(domain.getSortOrder());
        entity.setPrice(domain.getPrice());
        entity.setStock(domain.getStock());
        entity.setBinaryData(BinaryDataEntityMapper.toEntity(domain.getBinaryData()));
        entity.setTranslations(mapTranslationsToEntity(domain.getTranslations(), entity));
        entity.setCategory(CategoryEntityMapper.toEntity(domain.getCategory()));
        return entity;
    }

    public static Product toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product entity) {
        if (entity == null) {
            return null;
        }
        Product domain = new Product();
        domain.setId(entity.getId());
        domain.setIsActive(entity.getIsActive());
        domain.setSortOrder(entity.getSortOrder());
        domain.setPrice(entity.getPrice());
        domain.setStock(entity.getStock());
        domain.setBinaryData(BinaryDataEntityMapper.toDomain(entity.getBinaryData()));
        domain.setTranslations(mapTranslationsToDomain(entity.getTranslations()));
        domain.setCategory(CategoryEntityMapper.toDomain(entity.getCategory()));
        return domain;
    }

    private static List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation>
            mapTranslationsToEntity(
            List<ProductTranslation> translations,
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Product productEntity) {
        if (translations == null) {
            return null;
        }
        List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation> mapped =
                translations.stream()
                .map(ProductTranslationEntityMapper::toEntity)
                .collect(Collectors.toList());
        mapped.forEach(t -> t.setProduct(productEntity));
        return mapped;
    }

    private static List<ProductTranslation> mapTranslationsToDomain(
            List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(ProductTranslationEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
