package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "binary_data")
@Data
public class BinaryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentType;

    @Column(name = "data", columnDefinition = "bytea")
    @JsonIgnore
    private byte[] data;
}
