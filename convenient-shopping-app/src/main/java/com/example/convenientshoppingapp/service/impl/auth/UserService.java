package com.example.convenientshoppingapp.service.impl.auth;

import com.example.convenientshoppingapp.entity.auth.Role;
import com.example.convenientshoppingapp.entity.auth.User;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
