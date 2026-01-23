package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto;

import java.util.List;

public class ProductDto {
    private Long id;
    private Boolean isActive;
    private Integer sortOrder;
    private Double price;
    private Integer stock;
    private List<ProductTranslationDto> translations;
    private CategoryDto category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<ProductTranslationDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<ProductTranslationDto> translations) {
        this.translations = translations;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }
}
