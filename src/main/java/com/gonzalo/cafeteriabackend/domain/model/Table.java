package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Table {
    private Long id;
    private Integer number;
    private String status;
}
