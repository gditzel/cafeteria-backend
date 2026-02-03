package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal subtotal;
}
