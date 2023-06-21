package com.example.convenientshoppingapp.entity.auth;

import com.example.convenientshoppingapp.Utils.Gender;
import com.example.convenientshoppingapp.entity.BaseEntity;
import com.example.convenientshoppingapp.entity.Group;
import com.example.convenientshoppingapp.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
@Table(name = "user_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class Users extends BaseEntity implements UserDetails {
    private String username;
    private String password;

    private String email;

    @Column(name = "time_email_verified")
    private Timestamp timeEmailVerified;

    private Integer status;

    private String avatar;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String descriptions;

    private String fullname;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<Recipe> recipes = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    // liên kết 1-n giữa id của trưởng nhóm và group
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Group> group = new ArrayList<>();

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = roles
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
