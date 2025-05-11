package com.example.authservice.service.impl;

import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.UserResponseDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.RoleService;
import com.example.authservice.util.JwtUtil;
import com.example.authservice.service.UserService;
import com.example.authservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String registerUser(RegisterRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role defaultRole = roleService.findOrCreateRole("USER");

        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public String loginUser(LoginRequestDto loginDto) {
        User dbUser = userRepository.getByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), dbUser.getPassword())) {
            return "Invalid credentials";
        }

        return jwtUtil.generateToken(dbUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }
}