package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.ProductDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.ProductTranslationDto;
import com.gonzalo.cafeteriabackend.domain.model.Category;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.domain.model.ProductTranslation;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    private ProductMapper() {
    }

    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setIsActive(product.getIsActive());
        dto.setSortOrder(product.getSortOrder());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setTranslations(mapTranslationsToDto(product.getTranslations()));
        dto.setCategory(CategoryMapper.toDto(product.getCategory()));
        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setIsActive(dto.getIsActive());
        product.setSortOrder(dto.getSortOrder());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setTranslations(mapTranslationsToEntity(dto.getTranslations()));
        product.setCategory(mapCategory(dto.getCategory()));
        return product;
    }

    private static List<ProductTranslationDto> mapTranslationsToDto(List<ProductTranslation> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(ProductTranslationMapper::toDto)
                .collect(Collectors.toList());
    }

    private static List<ProductTranslation> mapTranslationsToEntity(List<ProductTranslationDto> translations) {
        if (translations == null) {
            return null;
        }
        return translations.stream()
                .map(ProductTranslationMapper::toEntity)
                .collect(Collectors.toList());
    }

    private static Category mapCategory(com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.CategoryDto dto) {
        return CategoryMapper.toEntity(dto);
    }
}
