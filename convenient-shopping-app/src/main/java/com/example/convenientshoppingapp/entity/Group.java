package com.example.convenientshoppingapp.entity;

import com.example.convenientshoppingapp.entity.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_list")
public class Group extends BaseEntity{
    private String name;

    @Column(name = "owner_id", insertable=false, updatable=false)
    private Long groupLeader;
    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="group_member", joinColumns=@JoinColumn(name="group_id"), inverseJoinColumns=@JoinColumn(name="user_id"))
    private List<User> users;

    @ManyToOne
    @JoinColumn(name="owner_id", insertable = false, updatable = false)
    private User owner;
}
