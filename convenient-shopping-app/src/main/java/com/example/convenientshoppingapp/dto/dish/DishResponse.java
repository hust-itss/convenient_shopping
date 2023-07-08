package com.example.convenientshoppingapp.dto.dish;

import com.example.convenientshoppingapp.dto.recipe.RecipeResponse;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DishResponse {
    private Timestamp cookDate;
    private Long id;
    private Integer cookMeal;
    private Long recipeId;
    private Long userId;
    private RecipeResponse recipe;
    private Boolean isCook;
    private Boolean isStoredFridge;
    private String descriptions;
    private Timestamp expireAt;
}
