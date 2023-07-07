package com.example.convenientshoppingapp.Utils;

import com.example.convenientshoppingapp.entity.auth.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static Long getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        return userId;
    }
}
