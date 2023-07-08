package com.example.convenientshoppingapp.dto.dish;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CreateDishRequest {
    private Long recipeId;
    private Timestamp cookDate;
    private Integer cookMeal;
    private String descriptions;

}
