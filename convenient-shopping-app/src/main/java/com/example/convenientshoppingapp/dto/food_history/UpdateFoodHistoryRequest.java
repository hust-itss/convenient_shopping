package com.example.convenientshoppingapp.dto.food_history;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UpdateFoodHistoryRequest {
    private Double quantity;
    private Double price;
    private Long measureId;
    private Timestamp buyAt;
    private String buyAddress;
    private int isStoredInFridge;
    private Timestamp expireAt;
    private Boolean isBought;
}
