package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FoodHistoryRepository extends JpaRepository<FoodHistory, Long>, JpaSpecificationExecutor<FoodHistory> {
}
