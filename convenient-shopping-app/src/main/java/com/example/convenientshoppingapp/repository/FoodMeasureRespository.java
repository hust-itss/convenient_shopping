package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.FoodMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodMeasureRespository extends JpaRepository<FoodMeasure, Long> {
    Boolean existsByName(String name);
}
