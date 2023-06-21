package com.example.convenientshoppingapp.dto;

import jakarta.validation.constraints.AssertFalse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFoodDto {
    private Long recipeId;
    private Long foodId;

}
