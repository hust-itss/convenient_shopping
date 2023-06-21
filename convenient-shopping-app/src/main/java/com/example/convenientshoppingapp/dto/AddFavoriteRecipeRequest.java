package com.example.convenientshoppingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFavoriteRecipeRequest {

    @NotNull(message = "Vui lòng nhập mã công thức")
    private Long recipeId;
}
