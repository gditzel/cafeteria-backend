package com.gonzalo.cafeteriabackend.application.port.in.user;

import com.gonzalo.cafeteriabackend.domain.model.User;
import java.util.List;

public interface GetWaiters {
    GetWaitersResponse execute(GetWaitersRequest request);

    record GetWaitersRequest() {}

    record GetWaitersResponse(List<User> users) {}
}
