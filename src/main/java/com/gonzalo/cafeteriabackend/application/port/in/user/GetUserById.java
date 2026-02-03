package com.gonzalo.cafeteriabackend.application.port.in.user;

import com.gonzalo.cafeteriabackend.domain.model.User;

public interface GetUserById {
    GetUserByIdResponse execute(GetUserByIdRequest request);

    record GetUserByIdRequest(Long id) {}

    record GetUserByIdResponse(User user) {}
}
