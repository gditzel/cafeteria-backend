package com.gonzalo.cafeteriabackend.application.port.in;

import com.gonzalo.cafeteriabackend.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    Optional<User> findByUsername(String username);

    List<User> getWaiters();

    User getUserById(Long id);

    User saveUser(User user);

    void deleteUser(Long id);
}
