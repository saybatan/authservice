package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}