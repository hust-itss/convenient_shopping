package com.example.convenientshoppingapp.repository;
import com.example.convenientshoppingapp.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface FoodHistoryRepository extends JpaRepository<FoodHistory, Long>, JpaSpecificationExecutor<FoodHistory> {

    Optional<FoodHistory> findFoodHistoryByIdAndUserId(Long id, Long userId);
    Optional<FoodHistory> findFoodHistoryByIdAndGroupId(Long id, Long groupId);

    List<FoodHistory> findAllByGroupId(Long groupId);

    List<FoodHistory> findAllDistinctByFoodIdInAndUserIdAndExpireAtGreaterThanEqualAndBuyAtNotNullOrderByBuyAtAsc(List<Long> foodIds, Long userId, Timestamp today);

    @Query(value = "SELECT food.name, food.poster_link as posterLink, SUM(history_food.price) as totalPrice, COUNT(history_food.id) as countBuy " +
            "FROM history_food INNER JOIN food ON food.id = history_food.food_id " +
            "WHERE history_food.buy_at IS NOT NULL" +
            " AND ( 1 = :startDate OR history_food.create_at >= :startDate ) " +
            " AND ( 1 = :endDate OR history_food.create_at <= :endDate )" +
            " AND ( 0 = :userId OR (history_food.user_id = :userId AND history_food.group_id IS NULL)) " +
            " AND ( 0 = :groupId OR history_food.group_id = :groupId) " +
            " GROUP BY food_id",
            nativeQuery = true)

    List<Object> statisticsFood(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("userId") Long userId, @Param("groupId") Long groupId);
}
