package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.food.CreateFoodMeasureRequest;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.FoodMeasureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food_measure")
@RequiredArgsConstructor
public class FoodMeasureController {
    private final FoodMeasureService foodMeasureService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody CreateFoodMeasureRequest createFoodMeasureRequest) {
        return foodMeasureService.create(createFoodMeasureRequest);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> get() {
        return foodMeasureService.getAll();
    }
}
