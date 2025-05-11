package com.example.authservice.controller;

import com.example.authservice.dto.UserResponseDto;
import com.example.authservice.entity.Role;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.response.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<RestResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "User list retrieved successfully!", users));
    }
}
