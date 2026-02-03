package com.gonzalo.cafeteriabackend.application.port.out;

import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    List<User> findByRole(Role role);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);
}
