package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto;

import java.util.List;

public class CategoryDto {
    private Long id;
    private Boolean isActive;
    private Integer sortOrder;
    private List<CategoryTranslationDto> translations;
    private Integer productCount;

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

    public List<CategoryTranslationDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<CategoryTranslationDto> translations) {
        this.translations = translations;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
}
