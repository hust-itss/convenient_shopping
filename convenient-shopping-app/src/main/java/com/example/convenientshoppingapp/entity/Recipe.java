package com.example.convenientshoppingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe extends BaseEntity {

    @Column(name = "date_cook")
    private Timestamp dateCook;

    private String description;

    private Integer status;

    private  Timestamp expired;
}
