package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.UserUseCase;
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
    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        return userUseCase.findByUsername(loginRequest.getUsername())
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
        return userUseCase.getWaiters().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userUseCase.getUserById(id);
        return user != null
                ? ResponseEntity.ok(UserMapper.toDto(user))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toDto(userUseCase.saveUser(UserMapper.toEntity(user))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
