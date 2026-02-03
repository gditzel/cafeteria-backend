package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalo.cafeteriabackend.application.port.in.user.DeleteUser;
import com.gonzalo.cafeteriabackend.application.port.in.user.FindUserByUsername;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetUserById;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetWaiters;
import com.gonzalo.cafeteriabackend.application.port.in.user.SaveUser;
import com.gonzalo.cafeteriabackend.domain.model.Role;
import com.gonzalo.cafeteriabackend.domain.model.User;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.UserDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.UserLoginRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FindUserByUsername findUserByUsername;

    @MockBean
    private GetWaiters getWaiters;

    @MockBean
    private GetUserById getUserById;

    @MockBean
    private SaveUser saveUser;

    @MockBean
    private DeleteUser deleteUser;

    @Test
    void loginReturnsUserWhenActive() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("ana");
        user.setEmail("ana@mail.com");
        user.setIsActive(true);
        user.setRole(Role.WAITER);

        when(findUserByUsername.execute(new FindUserByUsername.FindUserByUsernameRequest("ana")))
                .thenReturn(new FindUserByUsername.FindUserByUsernameResponse(Optional.of(user)));

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("ana");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("ana"))
                .andExpect(jsonPath("$.email").value("ana@mail.com"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.role").value("WAITER"));
    }

    @Test
    void loginReturnsUnauthorizedWhenInactive() throws Exception {
        User user = new User();
        user.setUsername("ana");
        user.setIsActive(false);

        when(findUserByUsername.execute(new FindUserByUsername.FindUserByUsernameRequest("ana")))
                .thenReturn(new FindUserByUsername.FindUserByUsernameResponse(Optional.of(user)));

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("ana");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Usuario inactivo"));
    }

    @Test
    void loginReturnsNotFoundWhenMissing() throws Exception {
        when(findUserByUsername.execute(new FindUserByUsername.FindUserByUsernameRequest("ana")))
                .thenReturn(new FindUserByUsername.FindUserByUsernameResponse(Optional.empty()));

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("ana");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    void getWaitersReturnsList() throws Exception {
        User waiter1 = new User();
        waiter1.setId(1L);
        waiter1.setUsername("ana");
        waiter1.setRole(Role.WAITER);

        User waiter2 = new User();
        waiter2.setId(2L);
        waiter2.setUsername("juan");
        waiter2.setRole(Role.WAITER);

        when(getWaiters.execute(new GetWaiters.GetWaitersRequest()))
                .thenReturn(new GetWaiters.GetWaitersResponse(List.of(waiter1, waiter2)));

        mockMvc.perform(get("/api/users/waiters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("ana"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("juan"));
    }

    @Test
    void getUserByIdReturnsNotFoundWhenMissing() throws Exception {
        when(getUserById.execute(new GetUserById.GetUserByIdRequest(9L)))
                .thenReturn(new GetUserById.GetUserByIdResponse(null));

        mockMvc.perform(get("/api/users/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveUserReturnsCreatedDto() throws Exception {
        User saved = new User();
        saved.setId(7L);
        saved.setUsername("maria");
        saved.setEmail("maria@mail.com");
        saved.setIsActive(true);
        saved.setRole(Role.ADMIN);

        when(saveUser.execute(any(SaveUser.SaveUserRequest.class)))
                .thenReturn(new SaveUser.SaveUserResponse(saved));

        UserDto dto = new UserDto();
        dto.setUsername("maria");
        dto.setEmail("maria@mail.com");
        dto.setIsActive(true);
        dto.setRole(Role.ADMIN);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.username").value("maria"))
                .andExpect(jsonPath("$.email").value("maria@mail.com"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        ArgumentCaptor<SaveUser.SaveUserRequest> captor =
                ArgumentCaptor.forClass(SaveUser.SaveUserRequest.class);
        verify(saveUser).execute(captor.capture());
        assertThat(captor.getValue().user().getUsername()).isEqualTo("maria");
    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/3"))
                .andExpect(status().isNoContent());

        verify(deleteUser).execute(new DeleteUser.DeleteUserRequest(3L));
    }
}
