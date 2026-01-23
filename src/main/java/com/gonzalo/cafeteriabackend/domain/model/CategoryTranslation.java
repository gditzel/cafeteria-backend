package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryTranslation {
    private Long id;
    private String languageCode;
    private String name;
}
