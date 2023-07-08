package com.example.convenientshoppingapp.dto.food;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateFoodRequest {
    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Hình ảnh không được để trống")
    private String posterLink;

    @NotEmpty(message = "Mô tả không được để trống")
    private String description;

    private Integer quantity;

    private String measure;
}
