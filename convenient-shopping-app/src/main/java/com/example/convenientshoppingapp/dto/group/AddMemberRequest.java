package com.example.convenientshoppingapp.dto.group;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AddMemberRequest {
    @NotNull(message = "Danh sách tài khoản không được bỏ trống")
    private ArrayList<Long> listId;
}
