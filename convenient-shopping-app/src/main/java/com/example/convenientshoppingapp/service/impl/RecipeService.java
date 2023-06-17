package com.example.convenientshoppingapp.service.impl;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.repository.DishRepository;
import com.example.convenientshoppingapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final DishRepository dishRepository;

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
