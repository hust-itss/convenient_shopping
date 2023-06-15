package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@Valid @RequestBody Food food) {
        foodService.save(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", food));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @Valid @RequestBody Food food) {
        food.setId(id);
        foodService.update(food);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Cập nhật dữ liệu thành công", food));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", food));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", foodService.findFoodByName(page,size,name)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        foodService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Xóa dữ liệu thành công", ""));
    }
}
