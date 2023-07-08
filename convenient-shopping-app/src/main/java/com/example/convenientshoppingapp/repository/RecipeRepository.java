package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    void deleteRecipeById(Long id);

    Boolean existsRecipeById(Long id);

    Optional<Recipe> findById(Long id);

    Optional<Recipe> findByIdAndAndUserId(Long id, Long userId);

    Boolean existsByIdAndUserId(Long id, Long userId);
}
