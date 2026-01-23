package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Category {
    private Long id;
    private Boolean isActive = true;
    private Integer sortOrder = 0;
    private List<CategoryTranslation> translations;
    private Integer productCount;
}
