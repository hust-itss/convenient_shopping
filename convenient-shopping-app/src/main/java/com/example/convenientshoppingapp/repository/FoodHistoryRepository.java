package com.example.convenientshoppingapp.repository;
import com.example.convenientshoppingapp.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FoodHistoryRepository extends JpaRepository<FoodHistory, Long>, JpaSpecificationExecutor<FoodHistory> {

    Optional<FoodHistory> findFoodHistoryByIdAndUserId(Long id, Long userId);
}
