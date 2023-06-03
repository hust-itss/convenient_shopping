package com.example.convenientshoppingapp.entity;

import com.example.convenientshoppingapp.Utils.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity {

    @NotNull(message = "Please enter username")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 20 characters")
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    private String email;

    @Column(name = "time_email_verified")
    private Timestamp timeEmailVerified;
    @NotEmpty
    private Integer status;

    private String avatar;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cook_id"))
    private Set<Cook> cooks = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Group> group = new ArrayList<>();

}
