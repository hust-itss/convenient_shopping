package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodHistoryRepository extends JpaRepository<FoodHistory, Long> {
}
