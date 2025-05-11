package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.response.RestResponse;
import com.example.authservice.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Kullanıcı kimlik doğrulama işlemleri")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<RestResponse<String>> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role defaultRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER")));

        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);

        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "User registered successfully!", null));
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<String>> login(@RequestBody LoginRequestDto loginDto) {
        User dbUser = userRepository.getByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RestResponse<>(HttpStatus.UNAUTHORIZED, "Invalid credentials", null));
        }

        String token = jwtUtil.generateToken(dbUser);
        return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK, "Login successful!", token));
    }
}