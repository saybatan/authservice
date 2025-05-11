package com.example.authservice.service;

import com.example.authservice.entity.Role;

public interface RoleService {
    Role findOrCreateRole(String roleName);
}