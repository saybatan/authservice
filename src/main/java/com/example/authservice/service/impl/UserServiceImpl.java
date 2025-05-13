package com.example.authservice.service.impl;

import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.UpdateUserRequestDto;
import com.example.authservice.dto.UserResponseDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.exception.NotFoundException;
import com.example.authservice.exception.UnauthorizedException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.RoleService;
import com.example.authservice.util.JwtUtil;
import com.example.authservice.service.UserService;
import com.example.authservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
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

        Role userRole = roleService.findOrCreateRole("USER");
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        return "User registered successfully with default role: USER";
    }

    @Override
    public String loginUser(LoginRequestDto loginDto) {
        User dbUser = userRepository.getByUsername(loginDto.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), dbUser.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtUtil.generateToken(dbUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public String updateUser(UpdateUserRequestDto updateUserDto) {
        User user = userRepository.findByUsername(updateUserDto.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!isCurrentUserAdmin() && !user.getUsername().equals(getCurrentUsername())) {
            throw new UnauthorizedException("You can only update your own profile unless you are an ADMIN.");
        }

        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }

        if (updateUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        if (updateUserDto.getRoles() != null && isCurrentUserAdmin()) {
            Set<Role> updatedRoles = updateUserDto.getRoles().stream()
                    .map(roleService::findOrCreateRole)
                    .collect(Collectors.toSet());
            user.setRoles(updatedRoles);
        } else if (updateUserDto.getRoles() != null) {
            throw new UnauthorizedException("You cannot update roles! Only ADMIN users can modify roles.");
        }

        userRepository.save(user);
        return "User profile updated successfully!";
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}