package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRespository extends JpaRepository<Favorite, Long> {

    Boolean existsByUserIdAndRecipeId(Long userId, Long RecipeId);
}
