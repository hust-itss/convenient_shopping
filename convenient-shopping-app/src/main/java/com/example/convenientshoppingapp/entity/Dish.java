package com.example.convenientshoppingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dish")
public class Dish extends BaseEntity {
    @Column(name = "cook_date")
    private Timestamp cookDate;

    @Column(name = "cook_meal")
    private Integer cookMeal;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "user_id")
    private Long userId;

    private String descriptions;

    @Column(name = "is_stored_fridge")
    private Boolean isStoredFridge;

    @Column(name = "expire_at")
    private Timestamp expireAt;

    @Column(name = "is_cook")
    private Boolean isCook;

    @OneToOne()
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Recipe recipe;
}
