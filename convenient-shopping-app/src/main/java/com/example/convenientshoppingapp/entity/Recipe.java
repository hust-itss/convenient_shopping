package com.example.convenientshoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe extends BaseEntity{

    @Column(name = "dish_id")
    private Long dishId;

    private String descriptions;

    @ManyToMany(mappedBy = "recipes")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Food> foods = new HashSet<>();

    @ManyToMany(mappedBy = "recipes")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Users> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "dish_id",insertable=false, updatable=false, nullable = false)
    @JsonIgnore
    private Dish dish;

}
