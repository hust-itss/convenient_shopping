package com.example.convenientshoppingapp.dto.recipe;

import com.example.convenientshoppingapp.dto.auth.UserResponse;
import com.example.convenientshoppingapp.dto.food.FoodResponse;
import com.example.convenientshoppingapp.entity.Food;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class RecipeResponse {
    private Long id;
    private String name;
    private String posterLink;
    private String descriptions;
    private Long userId;
    private UserResponse owner;
    private List<FoodResponse> foods;
    private Timestamp createAt;
}
