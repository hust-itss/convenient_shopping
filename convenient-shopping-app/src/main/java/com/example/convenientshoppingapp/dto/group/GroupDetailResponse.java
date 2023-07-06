package com.example.convenientshoppingapp.dto.group;

import com.example.convenientshoppingapp.dto.auth.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupDetailResponse {
    private Long id;
    private String name;
    private List<UserResponse> users;
    private UserResponse owner;
}
