package com.gonzalo.cafeteriabackend.application.service;

import com.gonzalo.cafeteriabackend.application.port.in.UserUseCase;
import com.gonzalo.cafeteriabackend.application.port.out.UserRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserUseCase {
    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getWaiters() {
        return userRepository.findByRole(Role.WAITER);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
