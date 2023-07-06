package com.example.convenientshoppingapp.entity;

import com.example.convenientshoppingapp.entity.auth.User;
import jakarta.persistence.*;
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

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "measure_id")
    private Long measureId;

    @Column(name = "bought_by")
    private Long boughtBy;

    private Double price;

    @Column(name = "buy_at")
    private Timestamp buyAt;

    @Column(name = "buy_address")
    private String buyAddress;

    @Column(name = "is_stored_fridge")
    private int isStoredInFridge;

    @Column(name = "expire_at")
    private Timestamp expireAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", updatable = false, insertable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bought_by", updatable = false, insertable = false)
    private User userBought;

    @OneToOne(cascade = CascadeType.ALL, optional=false)
    @JoinColumn(name = "food_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Food food;

    @OneToOne(cascade = CascadeType.ALL, optional=false)
    @JoinColumn(name = "measure_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FoodMeasure measure;

}
