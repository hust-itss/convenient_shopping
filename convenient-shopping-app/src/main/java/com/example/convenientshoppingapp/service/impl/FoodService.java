package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;
    private final RecipeRepository recipeRepository;


    /**
     * Lưu food vào database
     * Nếu food đã tồn tại thì sẽ throw ra lỗi
     * Nếu food chưa tồn tại thì sẽ lưu vào database
     * Được sử dụng trong FoodController
     * @param food
     * @return
     */
    @Modifying
    public Food save(Food food) {
        if(foodRepository.findByName(food.getName()) != null){
            throw new RuntimeException("Food name already exists");
        }
        log.info("Food: {}", food);
        return foodRepository.save(food);
    }

    /**
     * Cập nhật food
     * Nếu không tìm thấy id của food thì sẽ throw ra lỗi
     * @param food
     * @return
     */
    @Modifying
    public Food update(Food food) {
        // Check nếu không tìm thấy id của food thì sẽ throw ra lỗi
        Food oldFood = foodRepository.findById(food.getId())
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + food.getId()));
        //set các thuộc tính mới cho oldFood để luưu vào database
        oldFood.setDescriptions(food.getDescriptions());
        oldFood.setName(food.getName());
        oldFood.setQuantity(food.getQuantity());
        oldFood.setMeasure(food.getMeasure());
        oldFood.setAddressBuy(food.getAddressBuy());
        oldFood.setBuyAt(food.getBuyAt());
        oldFood.setPrice(food.getPrice());
        oldFood.setPosterLink(food.getPosterLink());
        oldFood.setStatus(food.getStatus());
        oldFood.setUserId(food.getUserId());
        log.info("On update food: {}", oldFood);

        return foodRepository.save(oldFood);

    }


    /**
     * Tìm kiếm food theo id
     * @param id
     * @return
     */
    public Food findById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        log.info("On find food");
        return food;
    }

    /**
     * Xóa món ăn theo ID
     * @param id
     * @return
     */
    @Modifying
    public ResponseObject deleteById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        foodRepository.delete(food);
        return new ResponseObject("success", "Delete food successfully", "");
    }

    /**
     * Tìm kiếm food theo tên có phân trang và sắp xếp theo id
     * @param page
     * @param size
     * @param name
     * @return
     */
    public Map<String, Object> findFoodByName(int page, int size, String name) {
        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );

        Page<Food> pageFood;
        List<Food> foods = new ArrayList<Food>();
        pageFood = foodRepository.getFoodByNameContainingIgnoreCase(name, paging);
        foods = pageFood.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("foods", foods);
        response.put("currentPage", pageFood.getNumber());
        response.put("totalItems", pageFood.getTotalElements());
        response.put("totalPages", pageFood.getTotalPages());
        log.info("Get Food by name use paging successfully");
        return response;
    }

    /**
     * Xóa một loạt các food theo danh sách các id
     * @param ids
     */
    public void delete(Long[] ids){
        for (Long id : ids){
            foodRepository.deleteById(id);
        }
    }

    /**
     * Thêm một liên kết giữa food và recipe vào bảng food_recipe
     * @param foodId
     * @param recipeId
     */
    public void addRecipeToFood(Long foodId, Long recipeId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));
        if(food != null && recipe != null){
            if(food.getRecipes() == null){
                food.setRecipes(new HashSet<>());
            }
            food.getRecipes().add(recipe);
            recipe.getFoods().add(food);
        }
        foodRepository.save(food);
        recipeRepository.save(recipe);
        log.info("Add recipe to food successfully");
    }


    /**
     * Xóa một liên kết giữa food và recipe trong bảng food_recipe
     * @param foodId
     * @param recipeId
     */
    public void removeRecipeFromFood(Long foodId, Long recipeId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id:" + foodId));
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if(food != null && recipe.isPresent()){
            if(food.getRecipes() == null){
                food.setRecipes(new HashSet<>());
            }
            food.getRecipes().remove(recipe.get());
        }
        foodRepository.save(food);
        log.info("Remove recipe to food successfully");
    }

    /**
     * Lấy ra danh sách các recipe của food
     * @param foodId
     * @return
     */
    public Set<Recipe> getRecipesByFoodId(Long foodId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id:" + foodId));
        return food.getRecipes();
    }

    /**
     * Filter food theo ngày mua có phân trang
     * @param fromDate
     * @param toDate
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> filterFoodByBuyDate(Date fromDate, Date toDate, int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        Page<Food> foodPage = foodRepository.findByBuyAtBetween(fromDate, toDate, paging);
        log.info("On filter food by buy date");

        List<Food> filteredFoods = foodPage.getContent();

        // Tính tổng giá tiền các food đã mua
        int totalCost = 0;
        for (Food food : filteredFoods) {
            // Lấy số lượng food đã mua và giá tiền của mỗi food
            int quantity = food.getQuantity();
            // Giả sử giá tiền của mỗi food được lưu trong thuộc tính price (thêm thuộc tính này vào class Food nếu cần)
            Double price = food.getPrice();

            // Tính tổng giá tiền
            totalCost += quantity * price;
        }

        // Chuẩn bị kết quả trả về
        Map<String, Object> response = new HashMap<>();
        response.put("filteredFoods", filteredFoods);
        response.put("totalCost", totalCost);
        response.put("currentPage", foodPage.getNumber());
        response.put("totalItems", foodPage.getTotalElements());
        response.put("totalPages", foodPage.getTotalPages());
        return response;
    }



}
