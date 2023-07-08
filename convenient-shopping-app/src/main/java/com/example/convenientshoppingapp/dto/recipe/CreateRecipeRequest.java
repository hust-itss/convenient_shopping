package com.example.convenientshoppingapp.dto.recipe;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateRecipeRequest {
    private String name;
    private String descriptions;
    private String posterLink;
    private Set<Long> foodIds;
}
