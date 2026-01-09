package com.gonzalo.cafeteriabackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor // Esto reemplaza todos los getters, setters y constructor vacío
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}