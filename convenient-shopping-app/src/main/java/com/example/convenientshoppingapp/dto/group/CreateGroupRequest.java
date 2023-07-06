package com.example.convenientshoppingapp.dto.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequest {
    @NotEmpty(message = "Tên nhóm không được để trống")
    private String name;
}
