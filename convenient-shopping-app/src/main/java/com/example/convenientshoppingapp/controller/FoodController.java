package com.example.convenientshoppingapp.controller;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.service.impl.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/foods")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    /**
     * Tạo mới food
     * @param food
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseObject> save(@Valid @RequestBody Food food) {
        foodService.save(food);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", food));
    }

    /**
     * Cập nhật food
     * @param id
     * @param food
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable Long id, @Valid @RequestBody Food food) {
        food.setId(id);
        foodService.update(food);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Cập nhật dữ liệu thành công", food));
    }

    /**
     * Tìm kiếm food theo id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Food food = foodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", food));
    }

    /**
     * Tìm kiếm food theo name có phân trang
     * @param name
     * @param page
     * @param size
     * @return
     */
    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", foodService.findFoodByName(page,size,name)));
    }



    /**
     * Xóa food theo id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        foodService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foodService.deleteById(id));
    }

    /**
     * Thêm recipe vào food
     * @param foodId
     * @param recipeId
     * @return
     */
    @PostMapping("/addRecipe/{foodId}/recipe/{recipeId}")
    public ResponseEntity<ResponseObject> addRecipeToFood(@PathVariable Long foodId, @PathVariable Long recipeId) {
        foodService.addRecipeToFood(foodId, recipeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", ""));
    }

    /**
     * Xóa recipe khỏi food
     * @param foodId
     * @param recipeId
     * @return
     */
    @PostMapping("/removeRecipe/{foodId}/recipe/{recipeId}")
    public ResponseEntity<ResponseObject> removeRecipeFromFood(@PathVariable Long foodId, @PathVariable Long recipeId) {
        foodService.removeRecipeFromFood(foodId, recipeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Insert dữ liệu thành công", ""));
    }

    /**
     * Lấy danh sách recipe của food
     * @param foodId
     * @return
     */
    @GetMapping("/getRecipe/{foodId}")
    public ResponseEntity<ResponseObject> getRecipeFromFood(@PathVariable Long foodId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", foodService.getRecipesByFoodId(foodId)));
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseObject> filterFoodByBuyDate(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        try {
            Map<String, Object> response = foodService.filterFoodByBuyDate(fromDate, toDate, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Lấy dữ liệu thành công", response));
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
