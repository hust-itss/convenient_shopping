package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {


    Page<Food> getFoodByNameContainingIgnoreCase(String name, Pageable pageable);

    Food findByName(String name);

    @Query(value = "select r.id from food f join recipe_food rf on f.id = rf.food_id join recipe r on r.id = rf.recipe_id where f.id = :foodId", nativeQuery = true)
    Set<Recipe> findAllRecipeFromFood(@Param("foodId") Long foodId);
}
