package com.example.convenientshoppingapp.dto.auth;

import com.example.convenientshoppingapp.Utils.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String avatar;
    private String fullname;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
