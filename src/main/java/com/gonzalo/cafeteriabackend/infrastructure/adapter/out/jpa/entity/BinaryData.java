package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

@Entity
@Table(name = "binary_data")
@Data
public class BinaryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentType;

    @Lob
    @Column(name = "data")
    @JdbcTypeCode(java.sql.Types.VARBINARY) // <-- Esto le dice a Postgres que use 'bytea'
    private byte[] data;
}
