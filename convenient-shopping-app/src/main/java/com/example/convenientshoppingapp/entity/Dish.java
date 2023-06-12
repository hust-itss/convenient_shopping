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

    private String description;

    private Integer status;

    private  Timestamp expired;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<Recipe> recipes;
}
