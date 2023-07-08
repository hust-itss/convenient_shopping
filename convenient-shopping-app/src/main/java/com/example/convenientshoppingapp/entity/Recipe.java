package com.example.convenientshoppingapp.entity;
import com.example.convenientshoppingapp.entity.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe extends BaseEntity{
    private String name;
    @Column(name = "poster_link")
    private String posterLink;
    private String descriptions;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_food", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "food_id"))
    private List<Food> foods;

}
