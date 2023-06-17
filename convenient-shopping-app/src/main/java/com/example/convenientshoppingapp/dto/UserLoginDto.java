package com.example.convenientshoppingapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    @NotEmpty(message = "Vui lòng nhập tên đăng nhập")
    private String username;
    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    private String password;
}
