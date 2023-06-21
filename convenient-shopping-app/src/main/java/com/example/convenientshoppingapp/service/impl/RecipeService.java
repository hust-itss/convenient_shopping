package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Favorite;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.entity.ResponseObject;
import com.example.convenientshoppingapp.entity.auth.Users;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.FavoriteRespository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final DishRepository dishRepository;
    private final FavoriteRespository favoriteRespository;

    public ResponseEntity<ResponseObject> addFavorite(Long recipeId) {

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if(!recipe.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Công thức nấu ăn này không tồn tại", ""));
        }
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = users.getId();
        if(favoriteRespository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Công thức yêu thích đã có trong tài khoản của bạn", ""));
        }

        Favorite favorite = new Favorite().builder().recipeId(recipeId).userId(userId).build();

        favoriteRespository.save(favorite);
        if(favoriteRespository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObject("success", "Thêm công thức yêu thích thành công", ""));
        }
        return null;
    }

    @Modifying
    public Recipe save(Recipe recipe) {
        Dish dish = dishRepository.findById(recipe.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + recipe.getDishId()));
        recipe.setDish(dish);
        log.info("Recipe: {}", recipe);
        return recipeRepository.save(recipe);
    }

    @Modifying
    public Recipe update(Recipe recipe) {
        Recipe oldRecipe = recipeRepository.findById(recipe.getId())
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipe.getId()));
        oldRecipe.setDishId(recipe.getDishId());
        oldRecipe.setDescriptions(recipe.getDescriptions());
        oldRecipe.setDish(dishRepository.findById(recipe.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + recipe.getDishId())));
        log.info("Recipe: {}", recipe);
        return recipeRepository.save(oldRecipe);
    }

    public Map<String, Object> getAllRecipe(int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Recipe> pageRecipe;
        List<Recipe> recipes = new ArrayList<>();
        pageRecipe = recipeRepository.findAll(paging);
        recipes = pageRecipe.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("recipes", recipes);
        response.put("currentPage", pageRecipe.getNumber());
        response.put("totalItems", pageRecipe.getTotalElements());
        response.put("totalPages", pageRecipe.getTotalPages());
        log.info("get all recipe: {}", response);
        return response;
    }

    @Modifying
    public void deleteRecipeById(Long[] ids) {
        for(Long id: ids){
            recipeRepository.deleteRecipeById(id);
            log.info("delete recipe by id: {}", id);
        }
    }
}
