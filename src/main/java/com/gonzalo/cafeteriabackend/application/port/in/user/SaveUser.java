package com.gonzalo.cafeteriabackend.application.port.in.user;

import com.gonzalo.cafeteriabackend.domain.model.User;

public interface SaveUser {
    SaveUserResponse execute(SaveUserRequest request);

    record SaveUserRequest(User user) {}

    record SaveUserResponse(User user) {}
}
