package com.example.convenientshoppingapp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @Length(min = 6, max = 20, message = "Tên đăng nhập từ 6 đến 20 ký tự")
    private String username;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Địa chỉ email không hợp lệ")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min = 6, message = "Mật khẩu tôối thiểu 6 ký tự")
    private String password;
}
