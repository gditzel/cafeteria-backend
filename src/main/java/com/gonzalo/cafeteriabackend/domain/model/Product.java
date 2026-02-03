package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    private Long id;
    private Boolean isActive = true;
    private Integer sortOrder = 0;
    private Double price;
    private Integer stock;
    private BinaryData binaryData;
    private List<ProductTranslation> translations;
    private Category category;
}
