package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@RequestBody @Valid Dish dish){
        dishService.save(dish);
        return ResponseEntity.ok(new ResponseObject("success", "Insert dữ liệu thành công", dish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @RequestBody @Valid Dish dish){
        dish.setId(id);
        dishService.update(dish);
        return ResponseEntity.ok(new ResponseObject("success", "Cập nhật dữ liệu thành công", dish));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ){
        return ResponseEntity.ok(new ResponseObject("success", "Lấy dữ liệu thành công", dishService.findDishByName(page,size,name)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Dish dish = dishService.findById(id);
        return ResponseEntity.ok(new ResponseObject("success", "Lấy dữ liệu thành công", dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteById(@PathVariable Long id){
        dishService.deleteById(id);
        return ResponseEntity.ok(new ResponseObject("success", "Xóa dữ liệu thành công", null));
    }
}
