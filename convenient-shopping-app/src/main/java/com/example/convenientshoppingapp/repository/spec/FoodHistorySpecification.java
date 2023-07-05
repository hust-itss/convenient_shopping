package com.example.convenientshoppingapp.repository.spec;

import com.example.convenientshoppingapp.entity.Food;
import com.example.convenientshoppingapp.entity.FoodHistory;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class FoodHistorySpecification {
    public static Specification<FoodHistory> hasFoodContainName(String name) {
        return (root, query, criteriaBuilder) -> {
            Join<Food, FoodHistory> authorsBook = root.join("food");
            return criteriaBuilder.like(authorsBook.get("name"), "%" + name + "%");
        };
    }
}
