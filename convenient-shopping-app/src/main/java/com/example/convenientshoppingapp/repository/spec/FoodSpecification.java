package com.example.convenientshoppingapp.repository.spec;

import com.example.convenientshoppingapp.entity.Food;
import org.springframework.data.jpa.domain.Specification;

public class FoodSpecification {
    public static Specification<Food> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("name"), "%" + name + "%");
    }

}
