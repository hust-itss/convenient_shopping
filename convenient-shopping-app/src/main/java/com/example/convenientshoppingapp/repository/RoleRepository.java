package com.example.convenientshoppingapp.repository;

import com.example.convenientshoppingapp.entity.auth.ERole;
import com.example.convenientshoppingapp.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
