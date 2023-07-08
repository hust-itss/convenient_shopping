package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.Utils.UserUtil;
import com.example.convenientshoppingapp.dto.food.CreateFoodRequest;
import com.example.convenientshoppingapp.dto.food.FoodResponse;
import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.repository.FoodRepository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import com.example.convenientshoppingapp.repository.spec.MySpecification;
import com.example.convenientshoppingapp.repository.spec.SearchCriteria;
import com.example.convenientshoppingapp.repository.spec.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;
    private final RecipeRepository recipeRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Lưu food vào database
     * Nếu food đã tồn tại thì sẽ throw ra lỗi
     * Nếu food chưa tồn tại thì sẽ lưu vào database
     * Được sử dụng trong FoodController
     * @param foodRequest
     * @return
     */
    @Modifying
    public ResponseEntity<ResponseObject> save(CreateFoodRequest foodRequest) {
        if(foodRepository.findByName(foodRequest.getName()) != null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này đã tồn tại", ""));
        }
        Food food = modelMapper.map(foodRequest, Food.class);
        Long userId = UserUtil.getCurrentUserId();
        food.setOwnerId(userId);
        foodRepository.save(food);

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Tạo thực phẩm mới thành công", ""));
    }

    /**
     * Cập nhật food
     * Nếu không tìm thấy id của food thì sẽ throw ra lỗi
     * @param food
     * @return
     */
    @Modifying
    public ResponseEntity<ResponseObject> update(Food food) {
        // Check nếu không tìm thấy id của food thì sẽ throw ra lỗi
        Optional<Food> oldFood = foodRepository.findById(food.getId());
        if(!oldFood.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Thực phẩm này đã tồn tại", ""));
        }
        Food newFood = oldFood.get();
        //set các thuộc tính mới cho oldFood để luưu vào database
        newFood.setDescription(food.getDescription());
        newFood.setName(food.getName());
        newFood.setPosterLink(food.getPosterLink());
        //newFood.setStatus(food.getStatus());
        newFood.setOwnerId(food.getOwnerId());

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Cập nhật thực phẩm thành công", foodRepository.save(newFood)));

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
    public ResponseEntity<ResponseObject> deleteById(Long id) {
        Long userId = UserUtil.getCurrentUserId();
        Boolean isExist = foodRepository.existsByIdAndOwnerId(id, userId);
        if(!isExist) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Bạn không phải người thêm nên không thể xóa thực phẩm này", ""));
        }

        foodRepository.deleteByIdAndOwnerId(id, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Xóa thực phẩm thành công", ""));
    }

    /**
     * Tìm kiếm food theo tên có phân trang và sắp xếp theo id
     * @param page
     * @param size
     * @param name
     * @return
     */
    public Map<String, Object> findFoodByName(int page, int size, String name, String rawDate) {
        Specification<Food> spec = where(null);

        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.asc("id")
                )
        );

        if(!rawDate.isEmpty()) {
            LocalDate date = LocalDate.parse(rawDate);
            LocalDate nextDate = date.plusDays(1);
            MySpecification esFoodStartDate = new MySpecification();
            esFoodStartDate.add(new SearchCriteria("createAt", date, SearchOperation.DATE_START));
            esFoodStartDate.add(new SearchCriteria("createAt", nextDate, SearchOperation.DATE_END));
            spec = spec.and(esFoodStartDate);

        }

        if(name.length() > 0) {
            MySpecification esEmployeeCode = new MySpecification();
            esEmployeeCode.add(new SearchCriteria("name", name, SearchOperation.MATCH));
            spec = spec.and(esEmployeeCode);
        }

        Page<Food> pageFood;
        pageFood = foodRepository.findAll(spec, paging);
        List<Food> foods = pageFood.getContent();
        List<FoodResponse> foodResponses =  Arrays.asList(modelMapper.map(foods, FoodResponse[].class));

        Map<String, Object> response = new HashMap<>();
        response.put("foods", foodResponses);
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
//        // Check nếu không tìm thấy id của food thì sẽ throw ra lỗi
//        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
//        // Check nếu không tìm thấy id của recipe thì sẽ throw ra lỗi
//        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));
//        // Check nếu food và recipe không null thì mới thực hiện thêm vào bảng food_recipe
//        if(food != null && recipe != null){
//            if(food.getRecipes() == null){
//                food.setRecipes(new HashSet<>());
//            }
//            food.getRecipes().add(recipe);
//            recipe.getFoods().add(food);
//        }
//        foodRepository.save(food);
//        recipeRepository.save(recipe);
//        log.info("Add recipe to food successfully");
    }


    /**
     * Xóa một liên kết giữa food và recipe trong bảng food_recipe
     * @param foodId
     * @param recipeId
     */
    public void removeRecipeFromFood(Long foodId, Long recipeId){
//        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id:" + foodId));
//        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
//        if(food != null && recipe.isPresent()){
//            if(food.getRecipes() == null){
//                food.setRecipes(new HashSet<>());
//            }
//            food.getRecipes().remove(recipe.get());
//        }
//        foodRepository.save(food);
//        log.info("Remove recipe to food successfully");
    }

    /**
     * Lấy ra danh sách các recipe của food
     * @param foodId
     * @return
     */
    public Set<Recipe> getRecipesByFoodId(Long foodId){
//        Food food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found with id:" + foodId));
//        return food.getRecipes();
        return null;
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
//        Pageable paging = PageRequest.of(page, size);
//
//        Page<Food> foodPage = foodRepository.findByBuyAtBetween(fromDate, toDate, paging);
//        log.info("On filter food by buy date");
//
//        List<Food> filteredFoods = foodPage.getContent();
//
//        // Tính tổng giá tiền các food đã mua
//        int totalCost = 0;
//        for (Food food : filteredFoods) {
//            // Lấy số lượng food đã mua và giá tiền của mỗi food
//            //int quantity = food.getQuantity();
//            // Giả sử giá tiền của mỗi food được lưu trong thuộc tính price (thêm thuộc tính này vào class Food nếu cần)
//            //Double price = food.getPrice();
//
//            // Tính tổng giá tiền
//            //totalCost += quantity * price;
//        }
//
//        // Chuẩn bị kết quả trả về
//        Map<String, Object> response = new HashMap<>();
//        response.put("filteredFoods", filteredFoods);
//        response.put("totalCost", totalCost);
//        response.put("currentPage", foodPage.getNumber());
//        response.put("totalItems", foodPage.getTotalElements());
//        response.put("totalPages", foodPage.getTotalPages());
        return null;
    }

//

}
