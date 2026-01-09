package com.gonzalo.cafeteriabackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor // Esto elimina los warnings de "may use Lombok"
public class Order {

    public enum Status {
        PENDIENTE,
        ENVIADO,
        COMPLETADO,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDIENTE;

    // Ya no necesitas escribir los Getters y Setters manualmente
}