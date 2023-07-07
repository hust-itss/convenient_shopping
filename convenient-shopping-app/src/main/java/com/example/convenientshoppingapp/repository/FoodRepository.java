package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.Recipe;
import com.example.convenientshoppingapp.repository.spec.FoodSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {


    Page<Food> getFoodByNameContainingIgnoreCase(String name, Pageable pageable);

    Food findByName(String name);

    Food findFoodById(Long Id);

    @Query(value = "select r.id from food f join recipe_food rf on f.id = rf.food_id join recipe r on r.id = rf.recipe_id where f.id = :foodId", nativeQuery = true)
    Set<Recipe> findAllRecipeFromFood(@Param("foodId") Long foodId);

    //Page<Food> findByBuyAtBetween(Date fromDate, Date toDate, Pageable paging);

     Long deleteByIdAndOwnerId(Long foodId, Long userId);

    Boolean existsByIdAndOwnerId(Long foodId, Long userId);
}
