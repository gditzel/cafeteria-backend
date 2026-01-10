package com.gonzalo.cafeteriabackend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference; // Importante

@Entity
@Table(name = "product_translations")
@Data
public class ProductTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference // Evita el bucle infinito: no serializa el producto de vuelta
    private Product product;

    private String languageCode;
    private String name;
    private String description;
}