package com.example.authservice.service;

import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    String registerUser(RegisterRequestDto requestDto);

    String registerAdmin(RegisterRequestDto requestDto);

    String loginUser(LoginRequestDto loginDto);

    List<UserResponseDto> getAllUsers();
}