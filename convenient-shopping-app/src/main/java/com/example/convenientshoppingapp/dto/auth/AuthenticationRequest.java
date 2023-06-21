package com.example.convenientshoppingapp.dto.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AuthenticationRequest {
    @NotEmpty(message = "Vui lòng nhập tên đăng nhập")
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Length(min = 6)
    private String password;
}
