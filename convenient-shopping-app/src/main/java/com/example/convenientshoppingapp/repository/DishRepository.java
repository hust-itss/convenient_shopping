package com.example.convenientshoppingapp.repository;


import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.FoodHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long>, JpaSpecificationExecutor<Dish> {

    Optional<Dish> findById(Long id);

    Optional<Dish> findDishByIdAndUserId(Long id, Long userId);

}
