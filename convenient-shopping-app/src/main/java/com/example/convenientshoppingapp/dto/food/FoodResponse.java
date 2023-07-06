package com.example.convenientshoppingapp.dto.food;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodResponse {
    private Long id;
    private String name;
    private String posterLink;
    private String description;

}
