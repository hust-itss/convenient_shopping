package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.food.CreateFoodHistoryRequest;
import com.example.convenientshoppingapp.dto.food_history.UpdateFoodHistoryRequest;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.FoodHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food_history")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FoodHistoryController {
    private final FoodHistoryService foodHistoryService;
    @PostMapping ("")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody CreateFoodHistoryRequest createFoodHistoryRequest) {
        return foodHistoryService.create(createFoodHistoryRequest);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "", name = "start_date") String startDate,
            @RequestParam(defaultValue = "", name = "end_date") String endDate
    ) {
        return foodHistoryService.getAll(page, size, name, startDate, endDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> get(@PathVariable Long id) {
        return foodHistoryService.getDetail(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateBuyStatus(@PathVariable Long id, @Valid @RequestBody UpdateFoodHistoryRequest newValue) {
        return foodHistoryService.update(id, newValue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        return foodHistoryService.delete(id);
    }
}
