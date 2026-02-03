package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tables")
@Getter @Setter @NoArgsConstructor
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;
    private String status; // 'FREE' o 'OCCUPIED'
}
