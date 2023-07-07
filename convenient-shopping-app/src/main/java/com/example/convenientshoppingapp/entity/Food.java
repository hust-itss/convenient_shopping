package com.example.convenientshoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food")
public class Food extends BaseEntity{

    private String name;
    @Column(name = "poster_link")
    private String posterLink;

    @Column(name = "description")
    private String description;


    @Column(name = "owner_id")
    @JoinTable
    private Long ownerId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_food",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<Recipe> recipes = new HashSet<>();

    //
//    @ManyToMany(mappedBy = "recipes")
//    @Fetch(value = FetchMode.SELECT)
//    @JsonIgnore
//    private Set<User> users = new HashSet<>();

//    @ManyToMany(mappedBy = "foods")
//    @Fetch(value = FetchMode.SELECT)
//    @JsonIgnore
//    private Set<Group> groups = new HashSet<>();
}
