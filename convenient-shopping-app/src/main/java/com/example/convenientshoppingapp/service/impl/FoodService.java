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
        Optional<Recipe> recipe = Optional.ofNullable(recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId)));
        if(food != null && recipe.isPresent()){
            if(food.getRecipes() == null){
                food.setRecipes(new HashSet<>());
            }
            food.getRecipes().add(recipe.get());
        }
        foodRepository.save(food);
//        recipeRepository.save(recipe);
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


}
