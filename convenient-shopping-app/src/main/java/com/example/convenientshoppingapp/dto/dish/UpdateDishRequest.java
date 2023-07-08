package com.example.convenientshoppingapp.dto.dish;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class UpdateDishRequest {
    private Timestamp cookDate;
    private Integer cookMeal;
    private String descriptions;
    private Boolean isStoredFridge;
    private Timestamp expireAt;
    private Boolean isCook;
}
