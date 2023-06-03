package com.example.convenientshoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "cook")
public class Cook extends BaseEntity{

    @Column(name = "recipe_id")
    private Integer recipeId;

    private String description;

    @ManyToMany(mappedBy = "cooks")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Food> foods = new HashSet<>();

    @ManyToMany(mappedBy = "cooks")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Users> users = new HashSet<>();

}
