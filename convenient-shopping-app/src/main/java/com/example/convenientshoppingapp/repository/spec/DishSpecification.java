package com.example.convenientshoppingapp.repository.spec;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Recipe;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class DishSpecification {
    public static Specification<Dish> hasFoodContainName(String name) {
        return (root, query, criteriaBuilder) -> {
            Join<Recipe, Dish> authorsBook = root.join("recipe");
            return criteriaBuilder.like(authorsBook.get("name"), "%" + name + "%");
        };
    }
}
