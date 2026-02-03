package com.gonzalo.cafeteriabackend.application.port.in.user;

import com.gonzalo.cafeteriabackend.domain.model.User;
import java.util.Optional;

public interface FindUserByUsername {
    FindUserByUsernameResponse execute(FindUserByUsernameRequest request);

    record FindUserByUsernameRequest(String username) {}

    record FindUserByUsernameResponse(Optional<User> user) {}
}
