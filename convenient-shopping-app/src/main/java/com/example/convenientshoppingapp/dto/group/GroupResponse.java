package com.example.convenientshoppingapp.dto.group;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class GroupResponse {
    private Long id;
    private String name;
    private Long groupLeader;
    private String leaderName;
}
