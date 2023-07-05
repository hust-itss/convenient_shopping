package com.example.convenientshoppingapp.entity;

import com.example.convenientshoppingapp.entity.auth.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_list")
public class Group extends BaseEntity{
    private String name;

    @Column(name = "owner_id")
    private Long groupLeader;
    @ManyToOne
    @JoinColumn(insertable=false, updatable=false,name = "id")
    @JsonIgnore
    private Users user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_member",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_food",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))
    private Set<Food> foods = new HashSet<>();


}
