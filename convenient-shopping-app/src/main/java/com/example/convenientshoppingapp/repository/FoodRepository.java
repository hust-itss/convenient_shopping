package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {


    Page<Food> getFoodByNameContainingIgnoreCase(String name, Pageable pageable);

    Food findByName(String name);
}
