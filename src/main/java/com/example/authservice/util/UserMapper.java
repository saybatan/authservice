package com.example.authservice.util;

import com.example.authservice.dto.UserResponseDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }
}