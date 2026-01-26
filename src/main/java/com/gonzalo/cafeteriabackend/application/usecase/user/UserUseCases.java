package com.gonzalo.cafeteriabackend.application.usecase.user;

import com.gonzalo.cafeteriabackend.application.port.in.user.DeleteUser;
import com.gonzalo.cafeteriabackend.application.port.in.user.FindUserByUsername;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetUserById;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetWaiters;
import com.gonzalo.cafeteriabackend.application.port.in.user.SaveUser;
import com.gonzalo.cafeteriabackend.application.port.out.UserRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserUseCases implements
        FindUserByUsername,
        GetWaiters,
        GetUserById,
        SaveUser,
        DeleteUser {
    private final UserRepositoryPort userRepository;

    public UserUseCases(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public FindUserByUsernameResponse execute(FindUserByUsernameRequest request) {
        Optional<User> user = userRepository.findByUsername(request.username());
        return new FindUserByUsernameResponse(user);
    }

    @Override
    public GetWaitersResponse execute(GetWaitersRequest request) {
        List<User> users = userRepository.findByRole(Role.WAITER);
        return new GetWaitersResponse(users);
    }

    @Override
    public GetUserByIdResponse execute(GetUserByIdRequest request) {
        return new GetUserByIdResponse(userRepository.findById(request.id()).orElse(null));
    }

    @Override
    public SaveUserResponse execute(SaveUserRequest request) {
        return new SaveUserResponse(userRepository.save(request.user()));
    }

    @Override
    public DeleteUserResponse execute(DeleteUserRequest request) {
        userRepository.deleteById(request.id());
        return new DeleteUserResponse(true);
    }
}
