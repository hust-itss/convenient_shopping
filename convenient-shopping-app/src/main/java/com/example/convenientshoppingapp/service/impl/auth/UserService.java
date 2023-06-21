package com.example.convenientshoppingapp.service.impl.auth;

import com.example.convenientshoppingapp.entity.auth.Role;
import com.example.convenientshoppingapp.entity.auth.Users;

public interface UserService {
    Users saveUser(Users user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
