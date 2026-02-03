package com.gonzalo.cafeteriabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private Boolean isActive = true;
    private Role role;
}
