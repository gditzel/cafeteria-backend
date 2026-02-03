package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.user.DeleteUser;
import com.gonzalo.cafeteriabackend.application.port.in.user.FindUserByUsername;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetUserById;
import com.gonzalo.cafeteriabackend.application.port.in.user.GetWaiters;
import com.gonzalo.cafeteriabackend.application.port.in.user.SaveUser;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.UserDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.UserLoginRequest;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final FindUserByUsername findUserByUsername;
    private final GetWaiters getWaiters;
    private final GetUserById getUserById;
    private final SaveUser saveUser;
    private final DeleteUser deleteUser;

    public UserController(
            FindUserByUsername findUserByUsername,
            GetWaiters getWaiters,
            GetUserById getUserById,
            SaveUser saveUser,
            DeleteUser deleteUser) {
        this.findUserByUsername = findUserByUsername;
        this.getWaiters = getWaiters;
        this.getUserById = getUserById;
        this.saveUser = saveUser;
        this.deleteUser = deleteUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        var response = findUserByUsername
                .execute(new FindUserByUsername.FindUserByUsernameRequest(loginRequest.getUsername()));
        return response.user()
                .map(user -> {
                    if (user.getIsActive()) {
                        return ResponseEntity.ok(UserMapper.toDto(user));
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inactivo");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"));
    }

    @GetMapping("/waiters")
    public List<UserDto> getWaiters() {
        var response = getWaiters.execute(new GetWaiters.GetWaitersRequest());
        return response.users().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = getUserById.execute(new GetUserById.GetUserByIdRequest(id)).user();
        return user != null
                ? ResponseEntity.ok(UserMapper.toDto(user))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user) {
        var response = saveUser.execute(new SaveUser.SaveUserRequest(UserMapper.toEntity(user)));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toDto(response.user()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUser.execute(new DeleteUser.DeleteUserRequest(id));
        return ResponseEntity.noContent().build();
    }
}
