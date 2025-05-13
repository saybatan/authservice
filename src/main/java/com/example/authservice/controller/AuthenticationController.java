package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.response.RestResponse;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register/user")
    public ResponseEntity<RestResponse<String>> registerUser(@RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "User registration result:", userService.registerUser(requestDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<String>> login(@RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "Login successful!", userService.loginUser(loginDto)));
    }
}