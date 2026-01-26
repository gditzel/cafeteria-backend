package com.gonzalo.cafeteriabackend.application.port.in.user;

public interface DeleteUser {
    DeleteUserResponse execute(DeleteUserRequest request);

    record DeleteUserRequest(Long id) {}

    record DeleteUserResponse(boolean deleted) {}
}
