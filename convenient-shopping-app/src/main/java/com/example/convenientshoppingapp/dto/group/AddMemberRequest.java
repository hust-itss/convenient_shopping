package com.example.convenientshoppingapp.dto.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AddMemberRequest {
    @NotEmpty(message = "Username/Email không được bỏ trồng")
    private String username;
}
