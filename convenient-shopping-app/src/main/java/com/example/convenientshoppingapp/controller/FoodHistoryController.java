package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.FoodHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food_history")
@RequiredArgsConstructor
public class FoodHistoryController {
    private final FoodHistoryService foodHistoryService;
    @PostMapping ("")
    public ResponseEntity<ResponseObject> getAll(@Valid @RequestBody CreateFoodHistoryRequest createFoodHistoryRequest) {
        return foodHistoryService.create(createFoodHistoryRequest);
    }
}
