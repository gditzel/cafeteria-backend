package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.UserRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.UserRepository;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(UserEntityMapper.mapRoleToEntity(role)).stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserEntityMapper.toDomain(
                userRepository.save(UserEntityMapper.toEntity(user)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
