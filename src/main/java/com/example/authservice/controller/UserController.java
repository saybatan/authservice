package com.example.authservice.controller;

import com.example.authservice.dto.UpdateUserRequestDto;
import com.example.authservice.dto.UserResponseDto;
import com.example.authservice.response.RestResponse;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<RestResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "User list retrieved successfully!", userService.getAllUsers()));
    }

    @PutMapping("/update")
    public ResponseEntity<RestResponse<String>> updateUser(@RequestBody UpdateUserRequestDto updateUserDto) {
        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "Update result:", userService.updateUser(updateUserDto)));
    }
}
