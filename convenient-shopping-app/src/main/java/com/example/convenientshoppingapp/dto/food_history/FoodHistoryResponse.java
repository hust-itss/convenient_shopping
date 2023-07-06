package com.example.convenientshoppingapp.dto.food_history;
import com.example.convenientshoppingapp.dto.auth.UserResponse;
import com.example.convenientshoppingapp.dto.food.FoodMeasureResponse;
import com.example.convenientshoppingapp.dto.food.FoodResponse;
import com.example.convenientshoppingapp.entity.FoodMeasure;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class FoodHistoryResponse {
    private Long id;
    private Long foodId;
    private String foodName;
    private String foodImage;
    private Long userId;
    private String userBuyId;
    private Double quantity;
    private Long measureId;
    private String measureName;
    private Double price;
    private Timestamp buyAt;
    private String buyAddress;
    private int isStoredInFridge;
    private Timestamp expireAt;
    private FoodMeasureResponse measure;
    private UserResponse user;
    private FoodResponse food;
    private UserResponse userBought;
}
