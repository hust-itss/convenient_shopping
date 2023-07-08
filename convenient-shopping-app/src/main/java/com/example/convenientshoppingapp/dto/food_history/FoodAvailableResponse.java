package com.example.convenientshoppingapp.dto.food_history;

import com.example.convenientshoppingapp.dto.food.FoodResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodAvailableResponse {
    private List<FoodHistoryResponse> exist;
    private List<FoodResponse> notExist;
}
