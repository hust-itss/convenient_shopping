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
@Table(name = "history_food")
public class FoodHistory extends BaseEntity{
    @Column(name = "food_id")
    private Long foodId;
    @Column(name = "user_id")
    private Long userId;
    private Double quantity;
    @Column(name = "measure_id")
    private Long measureId;
    private Double price;
    @Column(name = "buy_at")
    private Timestamp buyAt;
    @Column(name = "buy_address")
    private String buyAddress;
    @Column(name = "is_stored_fridge")
    private int isStoredInFridge;
    @Column(name = "expire_at")
    private Timestamp expireAt;
}
