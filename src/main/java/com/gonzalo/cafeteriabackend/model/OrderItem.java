package com.gonzalo.cafeteriabackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference // Evita que el JSON intente volver a la orden (Bucle infinito)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER) // Obligatorio para evitar Error 500 al serializar
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private BigDecimal subtotal;
}