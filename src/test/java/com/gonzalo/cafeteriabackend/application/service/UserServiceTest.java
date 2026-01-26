package com.gonzalo.cafeteriabackend.application.usecase.user;

import com.gonzalo.cafeteriabackend.application.port.in.user.DeleteUser;
import com.gonzalo.cafeteriabackend.application.port.in.user.FindUserByUsername;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetUserById;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetWaiters;
import com.gonzalo.cafeteriabackend.application.port.in.user.SaveUser;
import com.gonzalo.cafeteriabackend.application.port.out.UserRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCasesTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserUseCases userUseCases;

    @Test
    void findByUsernameReturnsRepositoryResult() {
        User user = new User();
        user.setUsername("ana");

        when(userRepository.findByUsername("ana")).thenReturn(Optional.of(user));

        Optional<User> result = userUseCases
                .execute(new FindUserByUsername.FindUserByUsernameRequest("ana"))
                .user();

        assertThat(result).contains(user);
    }

    @Test
    void getWaitersUsesWaiterRole() {
        User waiter = new User();
        waiter.setRole(Role.WAITER);

        when(userRepository.findByRole(Role.WAITER)).thenReturn(List.of(waiter));

        List<User> result = userUseCases
                .execute(new GetWaiters.GetWaitersRequest())
                .users();

        assertThat(result).containsExactly(waiter);
    }

    @Test
    void getUserByIdReturnsUserWhenPresent() {
        User user = new User();
        user.setId(10L);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        User result = userUseCases
                .execute(new GetUserById.GetUserByIdRequest(10L))
                .user();

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByIdReturnsNullWhenMissing() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        User result = userUseCases
                .execute(new GetUserById.GetUserByIdRequest(10L))
                .user();

        assertThat(result).isNull();
    }

    @Test
    void saveUserDelegatesToRepository() {
        User user = new User();
        user.setUsername("ana");

        when(userRepository.save(user)).thenReturn(user);

        User result = userUseCases
                .execute(new SaveUser.SaveUserRequest(user))
                .user();

        assertThat(result).isEqualTo(user);
    }

    @Test
    void deleteUserDelegatesToRepository() {
        userUseCases.execute(new DeleteUser.DeleteUserRequest(5L));

        verify(userRepository).deleteById(5L);
    }
}
