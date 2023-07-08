package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.dto.dish.CreateDishRequest;
import com.example.convenientshoppingapp.dto.dish.UpdateDishRequest;
import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dish")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    /**
     * Tạo mới món ăn
     * @param dish
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@RequestBody @Valid CreateDishRequest dish){
        return dishService.save(dish);
    }

    /**
     * Cập nhật món ăn
     * @param id
     * @param dish
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @RequestBody @Valid UpdateDishRequest dishRequest){
        return dishService.update(id, dishRequest);
    }

    /**
     * Tìm kiếm món ăn theo tên có phân trang
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "", name = "startDate") String startDate,
            @RequestParam(defaultValue = "", name = "endDate") String endDate,
            @RequestParam(defaultValue = "0", name = "meal") int meal

    ){
        return dishService.getAll(page,size,name, startDate, endDate, meal);
    }

    /**
     * Tìm kiếm món ăn theo id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Dish dish = dishService.findById(id);
        return ResponseEntity.ok(new ResponseObject("success", "Lấy dữ liệu thành công", dish));
    }

    /**
     * Xóa món ăn theo id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteById(@PathVariable Long id){
        return dishService.deleteById(id);
    }
}
