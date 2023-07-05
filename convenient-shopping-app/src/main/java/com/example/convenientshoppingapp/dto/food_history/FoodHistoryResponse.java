package com.example.convenientshoppingapp.dto.food_history;
import com.fasterxml.jackson.annotation.JsonFormat;
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
}
