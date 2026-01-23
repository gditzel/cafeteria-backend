package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.UserDto;
import com.gonzalo.cafeteriabackend.domain.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setIsActive(user.getIsActive());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setIsActive(dto.getIsActive());
        user.setRole(dto.getRole());
        return user;
    }
}
