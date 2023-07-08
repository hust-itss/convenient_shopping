package com.example.convenientshoppingapp.repository.spec;

import com.example.convenientshoppingapp.entity.Dish;
import com.example.convenientshoppingapp.entity.Favorite;
import com.example.convenientshoppingapp.entity.Recipe;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {
    public static Specification<Recipe> isMyFavorite(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<Favorite, Recipe> authorsBook = root.join("favorite");
            return criteriaBuilder.equal(authorsBook.get("user_id"), userId);
        };
    }
}
