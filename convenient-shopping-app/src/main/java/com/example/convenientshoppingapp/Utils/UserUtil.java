package com.example.convenientshoppingapp.Utils;

import com.example.convenientshoppingapp.entity.auth.Users;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static Long getCurrentUserId() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = users.getId();
        return userId;
    }
}
