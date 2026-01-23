package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductTranslation {
    private Long id;
    private String languageCode;
    private String name;
    private String description;
}
