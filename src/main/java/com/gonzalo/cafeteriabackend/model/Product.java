package com.gonzalo.cafeteriabackend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Importante
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private Integer stock;
    private Boolean active = true;

    @Transient
    private String nameEs;

    @Transient
    private String nameEn;

    @Transient
    private String descriptionEs;

    @Transient
    private String descriptionEn;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private BinaryData image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Evita el bucle infinito: esta es la parte principal
    private List<ProductTranslation> translations = new ArrayList<>();

    public void setImageData(byte[] data, String contentType) {
        if (this.image == null) {
            this.image = new BinaryData();
        }
        this.image.setData(data);
        this.image.setContentType(contentType);
    }
}