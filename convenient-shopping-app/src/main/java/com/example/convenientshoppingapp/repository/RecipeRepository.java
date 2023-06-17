package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    Optional<Recipe> findByDishId(Long dishId);

    void deleteRecipeById(Long id);
}
