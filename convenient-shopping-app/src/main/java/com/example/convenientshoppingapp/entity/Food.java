package com.example.convenientshoppingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food")
public class Food extends BaseEntity{
    @Column(name = "poster_link")
    private String posterLink;

    private String description;

    @NotEmpty
    private Integer Status;

    @Column(name = "user_id")
    private Integer userId;

    private Timestamp time;

    @Column(name = "address_buy")
    private String addressBuy;

    private String measure;

    private Integer number;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "food_cook",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "cook_id"))
    private Set<Cook> cooks = new HashSet<>();

    @ManyToMany(mappedBy = "foods")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Users> users = new HashSet<>();


}
