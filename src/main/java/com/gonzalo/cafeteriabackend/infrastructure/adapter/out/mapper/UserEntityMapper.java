package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;

public class UserEntityMapper {
    private UserEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.User toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.User entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.User();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setIsActive(domain.getIsActive());
        entity.setRole(mapRoleToEntity(domain.getRole()));
        return entity;
    }

    public static User toDomain(com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.User entity) {
        if (entity == null) {
            return null;
        }
        User domain = new User();
        domain.setId(entity.getId());
        domain.setUsername(entity.getUsername());
        domain.setEmail(entity.getEmail());
        domain.setIsActive(entity.getIsActive());
        domain.setRole(mapRoleToDomain(entity.getRole()));
        return domain;
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Role mapRoleToEntity(Role role) {
        if (role == null) {
            return null;
        }
        return com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Role.valueOf(role.name());
    }

    public static Role mapRoleToDomain(com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Role role) {
        if (role == null) {
            return null;
        }
        return Role.valueOf(role.name());
    }
}
