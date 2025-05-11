package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;
    private Set<String> roles;
}